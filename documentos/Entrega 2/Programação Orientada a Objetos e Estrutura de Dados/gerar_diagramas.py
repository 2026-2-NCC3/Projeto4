"""Modelo conceitual de POO, inspirado no diagrama original da equipe.
Os getters/setters representam o desenho acadêmico, não classes Java existentes.
Requer reportlab. Gera PDF e draw.io da mesma fonte.
"""
from pathlib import Path
from xml.etree import ElementTree as ET
from reportlab.pdfgen import canvas
from reportlab.lib.colors import HexColor

ROOT = Path(__file__).resolve().parent
W, H = 1460, 1850
BW = 300
nodes = []
def box(key, title, x, y, fields):
    methods = []
    for name, typ in fields:
        cap = name[0].upper() + name[1:]
        methods += [f'+ get{cap}(): {typ}', f'+ set{cap}(valor: {typ}): void']
    nodes.append(dict(id=key, title=title, x=x, y=y, fields=fields,
                      methods=methods, h=48 + len(fields)*20 + 16 + len(methods)*20 + 12))

box('card', 'Card', 40, 120, [('id','UUID'),('numero','String')])
box('msg', 'Mensagem', 580, 120, [('id','UUID'),('conteudo','String'),('dataEnvio','Date')])
box('not', 'Notificacao', 1120, 120, [('id','UUID'),('titulo','String'),('lida','boolean')])
box('aluno', 'Usuario (Aluno)', 580, 460, [('id','UUID'),('nome','String'),('email','String')])
box('cert', 'Certificado', 40, 820, [('id','UUID'),('url','String'),('dataEmissao','Date')])
box('ins', 'Inscricao', 400, 820, [('id','UUID'),('dataInscricao','Date'),('status','String')])
box('pres', 'Presenca', 760, 820, [('id','UUID'),('dataRegistro','Date')])
box('res', 'ResultadoPerfil', 1120, 820, [('id','UUID'),('classificacao','String'),('dataRealizacao','Date')])
box('curso', 'Curso', 220, 1160, [('id','UUID'),('nome','String'),('cargaHoraria','int')])
box('atv', 'Atividade', 760, 1160, [('id','UUID'),('nome','String'),('data','Date')])
box('teste', 'TestePerfil', 1120, 1160, [('titulo','String'),('descricao','String')])
box('alt', 'Alternativa', 760, 1500, [('id','String'),('texto','String'),('area','String')])
box('perg', 'Pergunta', 1120, 1500, [('id','UUID'),('enunciado','String')])
byid={n['id']: n for n in nodes}
def point(key, side, fraction=.5):
    n=byid[key]; x,y,h=n['x'],n['y'],n['h']
    return {'top':(x+BW*fraction,y),'bottom':(x+BW*fraction,y+h),
            'left':(x,y+h*fraction),'right':(x+BW,y+h*fraction)}[side]
edges=[]
def connect(a,b,sa,sb,via=(),fa=.5,fb=.5):
    edges.append((a,b,sa,sb,fa,fb,[point(a,sa,fa),*via,point(b,sb,fb)]))
connect('card','aluno','bottom','top',[(190,400),(640,400)],fb=.2)
connect('msg','aluno','bottom','top')
connect('not','aluno','bottom','top',[(1270,400),(820,400)],fb=.8)
connect('aluno','cert','bottom','top',[(610,770),(190,770)],fa=.1)
connect('aluno','ins','bottom','top',[(670,790),(550,790)],fa=.3)
connect('aluno','pres','bottom','top',[(790,790),(910,790)],fa=.7)
connect('aluno','res','bottom','top',[(850,770),(1270,770)],fa=.9)
connect('cert','curso','bottom','top',[(190,1130),(280,1130)],fb=.2)
connect('ins','curso','bottom','top',[(550,1130),(460,1130)],fb=.8)
connect('pres','atv','bottom','top')
connect('curso','atv','right','left')
connect('res','teste','bottom','top')
connect('teste','perg','bottom','top')
connect('perg','alt','left','right',fa=.4,fb=.30625)

pdf=canvas.Canvas(str(ROOT/'UML_Entrega2.pdf'),pagesize=(W,H))
pdf.setTitle('Próxima Etapa - Diagrama de classes conceitual - Entrega 2')
pdf.setAuthor('Equipe Próxima Etapa')
mx=ET.Element('mxfile',host='app.diagrams.net')
d=ET.SubElement(mx,'diagram',id='poo',name='Diagrama de classes')
model=ET.SubElement(d,'mxGraphModel',page='1',pageWidth=str(W),pageHeight=str(H))
r=ET.SubElement(model,'root')
ET.SubElement(r,'mxCell',id='0'); ET.SubElement(r,'mxCell',id='1',parent='0')
def text_cell(id,text,x,y,w,h,size=14,parent='1'):
    c=ET.SubElement(r,'mxCell',id=id,value=text,style=f'text;html=0;align=left;verticalAlign=top;spacing=8;fontSize={size};fontColor=#000000;',vertex='1',parent=parent)
    ET.SubElement(c,'mxGeometry',x=str(x),y=str(y),width=str(w),height=str(h),attrib={'as':'geometry'})
for txt,y,size in [('Próxima Etapa - Diagrama de classes',45,24),('Entrega 2 | Modelo conceitual de POO',77,16)]:
    pdf.setFont('Helvetica-Bold' if size==24 else 'Helvetica',size)
    pdf.drawString(40,H-y,txt)
    text_cell('title'+str(y),txt,32,y-size,1380,35,size)
for n in nodes:
    x,y,h=n['x'],n['y'],n['h']; split=48+len(n['fields'])*20+8
    pdf.setFillColor(HexColor('#FFFFFF'));pdf.setStrokeColor(HexColor('#222222'))
    pdf.rect(x,H-y-h,BW,h,fill=1)
    pdf.setFillColor(HexColor('#F2F2F2'));pdf.rect(x,H-y-36,BW,36,fill=1)
    pdf.setFillColor(HexColor('#000000'));pdf.setFont('Helvetica-Bold',16)
    pdf.drawCentredString(x+BW/2,H-y-24,n['title'])
    pdf.line(x,H-y-split,x+BW,H-y-split)
    pdf.setFont('Helvetica',14)
    attrs=[f'- {name}: {typ}' for name,typ in n['fields']]
    for i,line in enumerate(attrs):pdf.drawString(x+10,H-y-56-i*20,line)
    for i,line in enumerate(n['methods']):pdf.drawString(x+10,H-y-split-24-i*20,line)
    c=ET.SubElement(r,'mxCell',id=n['id'],value=n['title'],style='swimlane;html=0;startSize=36;fillColor=#F2F2F2;swimlaneFillColor=#FFFFFF;strokeColor=#222222;fontColor=#000000;fontSize=16;fontStyle=1;',vertex='1',parent='1')
    ET.SubElement(c,'mxGeometry',x=str(x),y=str(y),width=str(BW),height=str(h),attrib={'as':'geometry'})
    text_cell(n['id']+'attrs','\n'.join(attrs),0,36,BW,split-36,parent=n['id'])
    text_cell(n['id']+'methods','\n'.join(n['methods']),0,split+4,BW,h-split-4,parent=n['id'])
    c=ET.SubElement(r,'mxCell',id=n['id']+'divider',style='line;strokeColor=#222222;',vertex='1',parent=n['id'])
    ET.SubElement(c,'mxGeometry',x='0',y=str(split),width=str(BW),height='0',attrib={'as':'geometry'})
for i,(a,b,sa,sb,fa,fb,pts) in enumerate(edges):
    for p,q in zip(pts,pts[1:]):pdf.line(p[0],H-p[1],q[0],H-q[1])
    anchors={'top':lambda f:(f,0),'bottom':lambda f:(f,1),'left':lambda f:(0,f),'right':lambda f:(1,f)}
    ax,ay=anchors[sa](fa); bx,by=anchors[sb](fb)
    c=ET.SubElement(r,'mxCell',id='edge'+str(i),source=a,target=b,edge='1',parent='1',style=f'edgeStyle=none;endArrow=none;strokeColor=#222222;exitX={ax};exitY={ay};entryX={bx};entryY={by};')
    g=ET.SubElement(c,'mxGeometry',relative='1',attrib={'as':'geometry'})
    arr=ET.SubElement(g,'Array',attrib={'as':'points'})
    for x,y in pts[1:-1]:ET.SubElement(arr,'mxPoint',x=str(x),y=str(y))
notes=['- atributo privado | + método público | linha: associação entre conceitos.',
       'Getters e setters representam o modelo acadêmico; não são um inventário das classes Java implementadas.',
       'Fonte: modelo original da equipe, adaptado ao domínio da Entrega 2. Detalhes e relações: README.md.']
for i,line in enumerate(notes):
    y=1790+i*20;pdf.setFont('Helvetica',13);pdf.drawString(40,H-y,line)
    text_cell('note'+str(i),line,32,y-15,1380,23,13)
pdf.save(); ET.indent(mx); ET.ElementTree(mx).write(ROOT/'UML_Entrega2.drawio',encoding='utf-8',xml_declaration=True)
print('Gerados PDF e draw.io: 1 página, 13 classes conceituais.')
