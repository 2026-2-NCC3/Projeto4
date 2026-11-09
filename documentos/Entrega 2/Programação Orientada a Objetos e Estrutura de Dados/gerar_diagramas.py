"""Gera PDF vetorial e draw.io editável da mesma fonte. Requer reportlab."""
from pathlib import Path
from xml.etree import ElementTree as ET
from reportlab.pdfgen import canvas
from reportlab.lib.colors import HexColor
import math
ROOT=Path(__file__).resolve().parent
W,H=1430,800
pages=[]
def node(id,title,x,y,lines,tag='domínio'):
    return dict(id=id,title=title,x=x,y=y,w=240,h=126,lines=lines,tag=tag)
def edge(a,b,points,label='',ends=('', ''),kind='association'):
    return dict(a=a,b=b,points=points,label=label,ends=ends,kind=kind)
pages.append(dict(title='Próxima Etapa - Diagrama de classes',
subtitle='Entrega 2 | Modelo conceitual simplificado: inscrições, presenças e certificados',nodes=[
node('aluno','Aluno',40,210,['- id: UUID','- nome: String','- email: String']),
node('ins','Inscrição',410,210,['- id: UUID','- dataInscrição: Date','- status: String']),
node('curso','Curso',780,210,['- id: UUID','- nome: String','- cargaHorária: int']),
node('cert','Certificado',1150,210,['- id: UUID','- url: String','- dataEmissão: Date']),
node('pres','Presença',40,480,['- id: UUID','- dataRegistro: Date']),
node('atv','Atividade',410,480,['- id: UUID','- nome: String','- data: Date']),
node('qr','CódigoQR',780,480,['- id: UUID','- hash: String','- validade: Date'])],edges=[
edge('aluno','ins',[(280,273),(410,273)],'realiza',('1','0..*')),
edge('ins','curso',[(650,273),(780,273)],'refere-se a',('0..*','1')),
edge('curso','cert',[(1020,273),(1150,273)],'referencia',('1','0..*')),
edge('aluno','cert',[(160,210),(160,150),(1270,150),(1270,210)],'recebe',('1','0..*')),
edge('aluno','pres',[(160,336),(160,480)],'registra',('1','0..*')),
edge('pres','atv',[(280,543),(410,543)],'refere-se a',('0..*','1')),
edge('curso','atv',[(900,336),(900,400),(530,400),(530,480)],'oferece',('1','0..*')),
edge('atv','qr',[(650,543),(780,543)],'possui',('1','0..*'))],notes=[
'Linha = associação | 1 = um | 0..* = zero ou mais | - = atributo privado no modelo conceitual.'
]))

pdf=canvas.Canvas(str(ROOT/'UML_Entrega2.pdf'),pagesize=(W,H))
pdf.setTitle('Próxima Etapa - Diagrama de classes simplificado - Entrega 2')
pdf.setAuthor('Equipe Próxima Etapa')
mx=ET.Element('mxfile',host='app.diagrams.net',version='24.0.0')
for page_no,p in enumerate(pages,1):
    pdf.setFillColor(HexColor('#000000'))
    pdf.setFont('Helvetica-Bold',18); pdf.drawString(40,H-58,p['title'])
    pdf.setFillColor(HexColor('#000000')); pdf.setFont('Helvetica',12); pdf.drawString(40,H-84,p['subtitle'])
    d=ET.SubElement(mx,'diagram',id=f'page{page_no}',name=p['title'])
    model=ET.SubElement(d,'mxGraphModel',page='1',pageWidth=str(W),pageHeight=str(H),grid='1',gridSize='10')
    root=ET.SubElement(model,'root');ET.SubElement(root,'mxCell',id='0');ET.SubElement(root,'mxCell',id='1',parent='0')
    for key,txt,y,size in [('title',p['title'],30,18),('subtitle',p['subtitle'],70,12)]:
        c=ET.SubElement(root,'mxCell',id=key,value=txt,style=f'text;html=0;align=left;fontSize={size};fontColor=#000000;',vertex='1',parent='1')
        ET.SubElement(c,'mxGeometry',x='40',y=str(y),width='1020',height='35',attrib={'as':'geometry'})
    for n in p['nodes']:
        x,y,w,h=n['x'],n['y'],n['w'],n['h']; base=H-y-h
        pdf.setStrokeColor(HexColor('#000000'));pdf.setLineWidth(0.8);pdf.setFillColor(HexColor('#FFFFFF'));pdf.rect(x,base,w,h,fill=1)
        pdf.setFillColor(HexColor('#000000'));pdf.setFont('Helvetica',9)
        pdf.setFont('Helvetica-Bold',14);pdf.drawCentredString(x+w/2,H-y-30,n['title']);pdf.line(x,H-y-48,x+w,H-y-48)
        pdf.setFont('Helvetica',11);pdf.setFillColor(HexColor('#000000'))
        for i,line in enumerate(n['lines']):pdf.drawString(x+12,H-y-68-i*19,line)
        val=n['title']
        c=ET.SubElement(root,'mxCell',id=n['id'],value=val,style='swimlane;startSize=48;rounded=0;whiteSpace=wrap;html=0;align=center;verticalAlign=middle;fillColor=#FFFFFF;swimlaneFillColor=#FFFFFF;strokeColor=#000000;fontColor=#000000;fontSize=12;',vertex='1',parent='1')
        ET.SubElement(c,'mxGeometry',x=str(x),y=str(y),width=str(w),height=str(h),attrib={'as':'geometry'})
        body=ET.SubElement(root,'mxCell',id=n['id']+'_body',value='\n'.join(n['lines']),style='text;html=0;align=left;verticalAlign=top;spacing=10;whiteSpace=wrap;fontColor=#000000;fontSize=11;',vertex='1',parent=n['id'])
        ET.SubElement(body,'mxGeometry',x='0',y='48',width=str(w),height=str(h-48),attrib={'as':'geometry'})
    for i,e in enumerate(p['edges']):
        pts=e['points'];pdf.setStrokeColor(HexColor('#000000'));pdf.setFillColor(HexColor('#000000'));pdf.setLineWidth(1.1)
        pdf.setDash(4,3) if e['kind'] in ('dependency','realization') else pdf.setDash()
        for (x1,y1),(x2,y2) in zip(pts,pts[1:]):pdf.line(x1,H-y1,x2,H-y2)
        pdf.setDash()
        if e['kind'] in ('dependency','realization'):
            a,b=pts[-2:];angle=math.atan2(b[1]-a[1],b[0]-a[0]);
            for off in [-0.45,0.45]:pdf.line(b[0],H-b[1],b[0]-10*math.cos(angle+off),H-b[1]+10*math.sin(angle+off))
            if e['kind']=='realization':pdf.line(b[0]-10*math.cos(angle-0.45),H-b[1]+10*math.sin(angle-0.45),b[0]-10*math.cos(angle+0.45),H-b[1]+10*math.sin(angle+0.45))
        # label on longest segment; avoids right-angle vertices
        a,b=max(zip(pts,pts[1:]),key=lambda pair:abs(pair[0][0]-pair[1][0])+abs(pair[0][1]-pair[1][1]));lx=(a[0]+b[0])/2;ly=(a[1]+b[1])/2
        pdf.setFont('Helvetica',10)
        if a[0]==b[0]:pdf.drawString(lx+9,H-ly,e['label'])
        else:pdf.drawCentredString(lx,H-ly+12,e['label'])
        for idx,mult in [(0,e['ends'][0]),(-1,e['ends'][1])]:
            if not mult:continue
            a=pts[idx];b=pts[1 if idx==0 else -2];dx,dy=b[0]-a[0],b[1]-a[1];length=math.hypot(dx,dy)
            pdf.setFont('Helvetica-Bold',11);pdf.drawString(a[0]+(-40 if dx < 0 else 12 if dx > 0 else 5),H-(a[1]+dy/length*18)-15,mult)
        style='edgeStyle=none;html=0;fontSize=11;endArrow='+('open;dashed=1;' if e['kind']=='dependency' else 'block;endFill=0;dashed=1;' if e['kind']=='realization' else 'none;')
        c=ET.SubElement(root,'mxCell',id=f'e{i}',value=e['label'],style=style,edge='1',parent='1',source=e['a'],target=e['b'])
        geo=ET.SubElement(c,'mxGeometry',relative='1',attrib={'as':'geometry'});
        for key,point in [('sourcePoint',pts[0]),('targetPoint',pts[-1])]:ET.SubElement(geo,'mxPoint',x=str(point[0]),y=str(point[1]),attrib={'as':key})
        if len(pts)>2:
            arr=ET.SubElement(geo,'Array',attrib={'as':'points'})
            for pt in pts[1:-1]:ET.SubElement(arr,'mxPoint',x=str(pt[0]),y=str(pt[1]))
        for suffix,idx,mult in [('s',0,e['ends'][0]),('t',-1,e['ends'][1])]:
            if not mult:continue
            a=pts[idx];b=pts[1 if idx==0 else -2];dx,dy=b[0]-a[0],b[1]-a[1];length=math.hypot(dx,dy)
            c=ET.SubElement(root,'mxCell',id=f'e{i}{suffix}',value=mult,style='text;html=0;align=left;fontSize=11;fontStyle=1;',vertex='1',parent='1')
            ET.SubElement(c,'mxGeometry',x=str(a[0]+(-40 if dx < 0 else 12 if dx > 0 else 5)),y=str(a[1]+dy/length*18),width='40',height='20',attrib={'as':'geometry'})
    pdf.setFillColor(HexColor('#000000'));pdf.setFont('Helvetica',10)
    # Explicações detalhadas ficam no guia; o desenho mantém apenas legenda e fonte.
    short_notes=[p['notes'][0], 'Recorte do domínio; não é um inventário de classes Java. Fontes e explicação: README.md e GUIA_POO.md.']
    for i,line in enumerate(short_notes):
        pdf.drawString(40,150-i*18,line)
        c=ET.SubElement(root,'mxCell',id=f'note{i}',value=line,style='text;html=0;align=left;fontSize=10;',vertex='1',parent='1')
        ET.SubElement(c,'mxGeometry',x='40',y=str(H-160+i*18),width='1020',height='20',attrib={'as':'geometry'})
    pdf.setFont('Helvetica',9);pdf.drawString(40,25,'Próxima Etapa - Diagrama de classes - Entrega 2');pdf.drawRightString(W-40,25,f'{page_no} / {len(pages)}')
    pdf.showPage()
pdf.save();ET.indent(mx);ET.ElementTree(mx).write(ROOT/'UML_Entrega2.drawio',encoding='utf-8',xml_declaration=True)
print('PDF e draw.io gerados:',len(pages),'páginas')
