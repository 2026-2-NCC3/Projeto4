package com.example.myapplication;
import static org.junit.Assert.*;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.recyclerview.widget.RecyclerView;
import org.junit.Test;
public class ProfileAppearanceTest extends DemoCertificateTest {
 @Test public void profileKeepsAppearanceAndDemoOffline() {
  android.content.Context context=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
  try(ActivityScenario<HubActivity> scenario=ActivityScenario.launch(new Intent(context,HubActivity.class).putExtra("section","perfil"))) {
   scenario.onActivity(activity->{
    RecyclerView list=activity.findViewById(R.id.resourceRecycler);
    RecyclerView.Adapter adapter=list.getAdapter();
    StringBuilder text=new StringBuilder();
    for(int i=0;i<adapter.getItemCount();i++) {
     RecyclerView.ViewHolder holder=adapter.createViewHolder(list,adapter.getItemViewType(i));
     adapter.bindViewHolder(holder,i);
     text.append(((android.widget.TextView)holder.itemView.findViewById(R.id.rowTitle)).getText());
    }
    assertTrue(text.toString(),text.toString().contains("Aparência"));
    assertTrue(text.toString(),text.toString().contains("Certificado de demonstração"));
   });
  }
 }
 @Test public void themePreferencePersistsAndRejectsUnknownModes() {
  android.content.Context context=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
  int previous=ThemePreference.get(context);
  try {
   for(int mode:new int[]{-1,1,2}) {
    androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().runOnMainSync(()->ThemePreference.set(context,mode));
    assertEquals(mode,ThemePreference.get(context.getApplicationContext()));
    assertEquals(mode,androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode());
   }
   assertThrows(IllegalArgumentException.class,()->ThemePreference.set(context,77));
   assertEquals(2,ThemePreference.get(context));
  } finally {
   androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().runOnMainSync(()->ThemePreference.set(context,previous));
  }
 }
 @Test public void failedRefreshStopsSpinnerAndNavigationCanLoadAgain() throws Exception {
  android.content.Context context=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
  try(ActivityScenario<HubActivity> scenario=ActivityScenario.launch(new Intent(context,HubActivity.class).putExtra("section","perfil"))) {
   long deadline=System.currentTimeMillis()+15000;
   java.util.concurrent.atomic.AtomicBoolean loading=new java.util.concurrent.atomic.AtomicBoolean(true);
   while(loading.get()&&System.currentTimeMillis()<deadline) {
    scenario.onActivity(a->loading.set(a.resourcesLoading()));
    Thread.sleep(100);
   }
   scenario.onActivity(a->{
    assertFalse(a.resourcesLoading());
    androidx.swiperefreshlayout.widget.SwipeRefreshLayout refresh=a.findViewById(R.id.refreshLayout);
    assertFalse(refresh.isRefreshing());
    a.refreshResources();
    assertTrue(a.resourcesLoading());
    a.refreshResources();
    a.findViewById(R.id.navCursos).performClick();
   });
   scenario.recreate();
  }
 }
 @Test public void completedWriteSupersedesLoadingGetAndOldCompletion() throws Exception {
  android.content.Context context=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
  java.lang.reflect.Field generation=HubActivity.class.getDeclaredField("generation");
  generation.setAccessible(true);
  java.lang.reflect.Method current=HubActivity.class.getDeclaredMethod("current",int.class);
  current.setAccessible(true);
  try(ActivityScenario<HubActivity> scenario=ActivityScenario.launch(new Intent(context,HubActivity.class).putExtra("section","perfil"))) {
   scenario.onActivity(a->{
    try {
     // All calls run in one main-thread turn, before any GET completion can paint.
     int before=generation.getInt(a);
     a.reloadAfterMutation(before);
     assertTrue(generation.getInt(a)>before);
     assertTrue(a.resourcesLoading());
     assertFalse((Boolean)current.invoke(a,before));
     int after=generation.getInt(a);
     a.reloadAfterMutation(before);
     assertEquals(after,generation.getInt(a));
    } catch(ReflectiveOperationException e) { throw new AssertionError(e); }
   });
  }
 }
 @Test public void selectedThemeControlsConfigurationAndDisablesForcedDark() {
  android.app.Instrumentation instrumentation=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation();
  android.content.Context context=instrumentation.getTargetContext();
  int previous=ThemePreference.get(context);
  try {
   for(int mode:new int[]{androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO,
       androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES}) {
    instrumentation.runOnMainSync(()->ThemePreference.set(context,mode));
    try(ActivityScenario<HubActivity> scenario=ActivityScenario.launch(new Intent(context,HubActivity.class).putExtra("section","perfil"))) {
     scenario.onActivity(a->{
      boolean dark=mode==androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
      int night=a.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
      assertEquals(dark?android.content.res.Configuration.UI_MODE_NIGHT_YES:android.content.res.Configuration.UI_MODE_NIGHT_NO,night);
      int expected=android.graphics.Color.parseColor(dark?"#101917":"#FBFBFB");
      assertEquals(expected,((android.graphics.drawable.ColorDrawable)a.findViewById(R.id.screenRoot).getBackground()).getColor());
      if(android.os.Build.VERSION.SDK_INT>=29) {
       android.util.TypedValue forceDark=new android.util.TypedValue();
       assertTrue("Theme must explicitly declare its force-dark policy",a.getTheme().resolveAttribute(android.R.attr.forceDarkAllowed,forceDark,true));
       assertEquals("Manufacturer forced dark must not override the selected theme",0,forceDark.data);
      }
     });
    }
   }
  } finally { instrumentation.runOnMainSync(()->ThemePreference.set(context,previous)); }
 }
}
