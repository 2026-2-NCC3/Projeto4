"""Gera diagramas das classes Java implementadas, com membros selecionados.
Requer reportlab. Fonte comum ao PDF e ao draw.io; sem alterar o aplicativo.
"""
from pathlib import Path
from xml.etree import ElementTree as ET
import math
from reportlab.pdfgen import canvas
from reportlab.pdfbase.pdfmetrics import stringWidth
from reportlab.lib.colors import HexColor
ROOT=Path(__file__).resolve().parent
W,H=1500,1450
BW,BH=370,230
pages=[]
def node(id,title,col,row,attrs,methods):
    return dict(id=id,title=title,x=40+col*520,y=120+row*320,attrs=attrs,methods=methods)
def edge(a,b,sa='right',sb='left',via=(),kind='use',label='usa',fa=.5,fb=.5):
    return dict(a=a,b=b,sa=sa,sb=sb,via=via,kind=kind,label=label,fa=fa,fb=fb)
pages.append(dict(title='Próxima Etapa - Diagrama de classes',nodes=[
node('service','ApiService «interface»',0,0,[],['+ get(...): Call<JsonElement>','+ post(...): Call<JsonElement>']),
node('api','ApiClient',1,0,['- API: ApiService {static}'],['+ request(...): JsonElement {static}','+ publicPost(...): JsonElement {static}']),
node('session','SessionManager',2,0,[],['+ get(c: Context): Session {static}','+ save(c: Context, s: Session): void {static}']),
node('hub','HubActivity',1,1,['- owner: String','- answers: Map<String, String>'],['- load(): void','- queueQr(raw: String): void']),
node('repo','AlunoRepository',0,2,['- helper: AlunoDbHelper'],['+ enqueue(owner: String, qr: QrPayload): void','+ checkins(...): List<Pending>']),
node('qr','QrPayload',1,2,['+ activityId: String {final}','+ qrToken: String {final}'],['+ parse(raw: String): QrPayload {static}']),
node('scheduler','SyncScheduler',2,2,[],['+ sync(c: Context, owner: String): void {static}','+ stop(c: Context): void {static}']),
node('db','AlunoDbHelper',0,3,[],['+ onCreate(db: SQLiteDatabase): void','+ onUpgrade(...): void']),
node('worker','SyncWorker',2,3,[],['+ doWork(): Result'])],edges=[
edge('api','service','left','right'),edge('api','session'),
edge('hub','api','top','bottom'),
edge('hub','repo','bottom','top',[(634,700),(225,700)],fa=.2),
edge('hub','qr','bottom','top'),
edge('hub','scheduler','bottom','top',[(856,700),(1265,700)],fa=.8),
edge('repo','db','bottom','top',kind='association',label='helper'),
edge('scheduler','worker','bottom','top',label='agenda'),
edge('worker','repo','left','bottom',[(1000,1195),(1000,1020),(354.5,1020)],fb=.85)
],notes=[]))

def anchor(n,side,f=.5):
 x,y=n['x'],n['y']
 return {'top':(x+BW*f,y),'bottom':(x+BW*f,y+BH),'left':(x,y+BH*f),'right':(x+BW,y+BH*f)}[side]
pdf=canvas.Canvas(str(ROOT/'UML_Entrega2.pdf'),pagesize=(W,H))
pdf.setTitle('Próxima Etapa - Classes implementadas - Entrega 2');pdf.setAuthor('Equipe Próxima Etapa')
mx=ET.Element('mxfile',host='app.diagrams.net')
for num,page in enumerate(pages,1):
 d=ET.SubElement(mx,'diagram',id='page'+str(num),name=page['title'])
 model=ET.SubElement(d,'mxGraphModel',page='1',pageWidth=str(W),pageHeight=str(H))
 r=ET.SubElement(model,'root');ET.SubElement(r,'mxCell',id='0');ET.SubElement(r,'mxCell',id='1',parent='0')
 def text(id,value,x,y,w,h,size=14,parent='1'):
  c=ET.SubElement(r,'mxCell',id=id,value=value,style=f'text;html=0;align=left;verticalAlign=top;spacing=8;fontSize={size};fontColor=#000000;',vertex='1',parent=parent)
  ET.SubElement(c,'mxGeometry',x=str(x),y=str(y),width=str(w),height=str(h),attrib={'as':'geometry'})
 for id,value,y,size in [('title',page['title'],45,24),('subtitle','Entrega 2 - Principais classes implementadas',77,16)]:
  pdf.setFont('Helvetica-Bold' if size==24 else 'Helvetica',size);pdf.drawString(40,H-y,value);text(id,value,32,y-size,1400,35,size)
 lookup={n['id']:n for n in page['nodes']}
 for n in page['nodes']:
  x,y=n['x'],n['y'];split=48+max(1,len(n['attrs']))*20+8
  pdf.setStrokeColor(HexColor('#222222'));pdf.setFillColor(HexColor('#FFFFFF'));pdf.rect(x,H-y-BH,BW,BH,fill=1)
  pdf.setFillColor(HexColor('#F2F2F2'));pdf.rect(x,H-y-36,BW,36,fill=1)
  pdf.setFillColor(HexColor('#000000'));pdf.setFont('Helvetica-Bold',16);pdf.drawCentredString(x+BW/2,H-y-24,n['title'])
  pdf.line(x,H-y-split,x+BW,H-y-split);pdf.setFont('Helvetica',14)
  for i,line in enumerate(n['attrs']):
   assert stringWidth(line,'Helvetica',14)<BW-20,line
   pdf.drawString(x+10,H-y-56-i*20,line)
  for i,line in enumerate(n['methods']):
   assert stringWidth(line,'Helvetica',14)<BW-20,line
   pdf.drawString(x+10,H-y-split-24-i*20,line)
  c=ET.SubElement(r,'mxCell',id=n['id'],value=n['title'],style='swimlane;html=0;startSize=36;fillColor=#F2F2F2;swimlaneFillColor=#FFFFFF;strokeColor=#222222;fontColor=#000000;fontSize=16;fontStyle=1;',vertex='1',parent='1')
  ET.SubElement(c,'mxGeometry',x=str(x),y=str(y),width=str(BW),height=str(BH),attrib={'as':'geometry'})
  text(n['id']+'attrs','\n'.join(n['attrs']),0,36,BW,split-36,parent=n['id'])
  text(n['id']+'methods','\n'.join(n['methods']),0,split+4,BW,BH-split-4,parent=n['id'])
  c=ET.SubElement(r,'mxCell',id=n['id']+'divider',style='line;strokeColor=#222222;',vertex='1',parent=n['id'])
  ET.SubElement(c,'mxGeometry',x='0',y=str(split),width=str(BW),height='0',attrib={'as':'geometry'})
 for i,e in enumerate(page['edges']):
  pts=[anchor(lookup[e['a']],e['sa'],e['fa']),*e['via'],anchor(lookup[e['b']],e['sb'],e['fb'])]
  pdf.setDash(5,3) if e['kind']!='association' else pdf.setDash()
  for a,b in zip(pts,pts[1:]):pdf.line(a[0],H-a[1],b[0],H-b[1])
  pdf.setDash()
  if e['kind']!='association':
   a,b=pts[-2:];angle=math.atan2(b[1]-a[1],b[0]-a[0]);ends=[(b[0]-12*math.cos(angle+off),b[1]-12*math.sin(angle+off)) for off in [-.45,.45]]
   if e['kind']=='realization':
    path=pdf.beginPath();path.moveTo(b[0],H-b[1]);path.lineTo(ends[0][0],H-ends[0][1]);path.lineTo(ends[1][0],H-ends[1][1]);path.close();pdf.setFillColor(HexColor('#FFFFFF'));pdf.drawPath(path,fill=1);pdf.setFillColor(HexColor('#000000'))
   else:
    for x,y in ends:pdf.line(b[0],H-b[1],x,H-y)
  a,b=max(zip(pts,pts[1:]),key=lambda pair:math.dist(*pair));x,y=(a[0]+b[0])/2,(a[1]+b[1])/2
  pdf.setFont('Helvetica',12)
  if a[0]==b[0]:pdf.drawString(x+8,H-y,e['label'])
  else:pdf.drawCentredString(x,H-y+10,e['label'])
  anchors={'top':(.5,0),'bottom':(.5,1),'left':(0,.5),'right':(1,.5)};ax,ay=anchors[e['sa']];bx,by=anchors[e['sb']];ax,ay=(e['fa'],ay) if e['sa'] in ('top','bottom') else (ax,e['fa']);bx,by=(e['fb'],by) if e['sb'] in ('top','bottom') else (bx,e['fb'])
  arrow='none;' if e['kind']=='association' else 'block;endFill=0;dashed=1;' if e['kind']=='realization' else 'open;dashed=1;'
  c=ET.SubElement(r,'mxCell',id='edge'+str(i),value=e['label'],source=e['a'],target=e['b'],edge='1',parent='1',style=f'edgeStyle=none;endArrow={arrow}strokeColor=#222222;exitX={ax};exitY={ay};entryX={bx};entryY={by};')
  g=ET.SubElement(c,'mxGeometry',relative='1',attrib={'as':'geometry'});arr=ET.SubElement(g,'Array',attrib={'as':'points'})
  for x,y in pts[1:-1]:ET.SubElement(arr,'mxPoint',x=str(x),y=str(y))
 notes=['+ público | - privado | ... parâmetros abreviados | seta tracejada: usa | linha contínua: possui referência.']
 for i,line in enumerate(notes):
  y=1390+i*20;pdf.setFont('Helvetica',12);pdf.drawString(40,H-y,line);text('note'+str(i),line,32,y-15,1400,22,12)
 pdf.setFont('Helvetica',10);pdf.drawRightString(W-40,15,f'{num}/{len(pages)}');pdf.showPage()
pdf.save();ET.indent(mx);ET.ElementTree(mx).write(ROOT/'UML_Entrega2.drawio',encoding='utf-8',xml_declaration=True)
print('Gerados PDF e draw.io: uma página, nove classes implementadas.')
