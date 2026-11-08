package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Owns the list view lifecycle; the host supplies the current screen's adapter. */
public class ResourceListFragment extends Fragment {
  public interface AdapterHost {
    RecyclerView.Adapter<?> resourceAdapter();
  }

  private RecyclerView list;

  public ResourceListFragment() {
    super(R.layout.fragment_resource_list);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    list = view.findViewById(R.id.resourceRecycler);
    list.setLayoutManager(new LinearLayoutManager(requireContext()));
    if (requireActivity() instanceof AdapterHost)
      setAdapter(((AdapterHost) requireActivity()).resourceAdapter());
  }

  public void setAdapter(RecyclerView.Adapter<?> adapter) {
    if (list == null)
      throw new IllegalStateException("A lista deve ter uma view ativa antes de receber dados.");
    list.setAdapter(adapter);
  }

  @Override
  public void onDestroyView() {
    if (list != null) list.setAdapter(null);
    list = null;
    super.onDestroyView();
  }
}
