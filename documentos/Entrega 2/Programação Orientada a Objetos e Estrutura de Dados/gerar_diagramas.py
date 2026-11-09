"""Gera PDF vetorial e draw.io editável da mesma fonte. Requer reportlab."""
from pathlib import Path
from xml.etree import ElementTree as ET
from reportlab.pdfgen import canvas
from reportlab.lib.colors import HexColor
import math
ROOT=Path(__file__).resolve().parent
W,H=1100,780
pages=[]
def node(id,title,x,y,lines,tag='domínio'):
    return dict(id=id,title=title,x=x,y=y,w=240,h=126,lines=lines,tag=tag)
def edge(a,b,points,label='',ends=('', ''),kind='association'):
    return dict(a=a,b=b,points=points,label=label,ends=ends,kind=kind)
pages.append(dict(title='01 · Cursos, inscrições e presença',subtitle='Modelo de domínio atual no código E2. Entidades não representam classes Java individuais.',nodes=[
node('aluno','Aluno',40,130,['id: UUID','nome: String','escola: String']),
node('ins','Inscrição',430,130,['id: UUID','user_id, course_id: UUID','status: String']),
node('curso','Curso',820,130,['id: UUID; título: String','cargaHorária, encontros: int','ativo: boolean; vagas: int']),
node('pres','Presença',40,430,['id: UUID','user_id, activity_id: UUID','scanned_at: data/hora']),
node('atv','Atividade',430,430,['id, course_id: UUID','título: String; data: data/hora','cancelada: boolean']),
node('qr','CódigoQR',820,430,['id, activity_id: UUID','token_hash: String','abertura, expiração: data/hora'])],edges=[
edge('aluno','ins',[(280,193),(430,193)],'possui',('1','0..*')),
edge('ins','curso',[(670,193),(820,193)],'vincula',('0..*','1')),
edge('aluno','pres',[(160,256),(160,430)],'registra',('1','0..*')),
edge('pres','atv',[(280,493),(430,493)],'refere-se a',('0..*','1')),
edge('curso','atv',[(940,256),(940,336),(550,336),(550,430)],'oferece',('1','0..*')),
edge('atv','qr',[(670,493),(820,493)],'recebe',('1','0..*'))],notes=[
'Associação: linha contínua; números junto às extremidades indicam multiplicidade. UUIDs circulam como String no Java.',
'Unicidade: (aluno, curso) em Inscrição; (aluno, atividade) em Presença. Check-in valida inscrição, janela e atividade no servidor.',
'CódigoQR guarda hash, não token em claro. O payload lido contém v, activityId e qrToken. Curso é obtido pela Atividade.',
'Fontes: Backend/src/app.js; Backend/sql/001_entrega2.sql; Backend/docs/http-contract.md. Banco remoto depende da migração.' ]))
pages.append(dict(title='02 · Documentos e comunicação',subtitle='Modelo de domínio atual. Card é uma projeção da API; comunicado e notificação são conceitos distintos.',nodes=[
node('aluno','Aluno',40,130,['id: UUID','identidade: auth.users','perfil: profiles']),
node('cert','Certificado',430,130,['id, user_id, course_id: UUID','certificate_url: HTTPS','issued_at: data/hora']),
node('curso','Curso',820,130,['id: UUID','título: String','cargaHorária: int']),
node('card','Card',40,430,['id: UUID do aluno','nome, escola, número: String','derivado; sem tabela própria'],'projeção'),
node('msg','Mensagem',430,430,['id, user_id: UUID','remetente: aluno ou equipe','conteúdo; created_at']),
node('notif','Notificação',820,430,['id: UUID; user_id: UUID?','título, conteúdo: String','created_at: data/hora'])],edges=[
edge('aluno','cert',[(280,193),(430,193)],'obtém',('1','0..*')),
edge('cert','curso',[(670,193),(820,193)],'refere-se a',('0..*','1')),
edge('aluno','card',[(160,256),(160,430)],'consulta',('1','1')),
edge('aluno','msg',[(280,230),(350,230),(350,390),(550,390),(550,430)],'conversa',('1','0..*'))],notes=[
'Mensagem.user_id identifica o aluno dono da conversa; remetente distingue aluno e equipe, sem criar outra entidade aluno.',
'Notificação.user_id: 0..1 destinatário específico; null indica aviso geral. Cada aluno recebe 0..* avisos autorizados.',
'LeituraNotificação (notification_id, user_id, read_at) liga Aluno e Notificação, ambos 1 para 0..* leituras; par único.',
'Comunicado (id UUID, título, conteúdo, created_at) é publicação geral em e2_announcements; 0..* comunicados disponíveis.',
'Fontes: Backend/src/app.js (/card, /certificados, /mensagens, /notificacoes); Backend/sql/001_entrega2.sql.' ]))
pages.append(dict(title='03 · Orientação por questionário',subtitle='Modelo de domínio atual. TestePerfil é a visão agregada; alternativas e respostas são armazenadas em JSON.',nodes=[
node('teste','TestePerfil',40,130,['título, descrição: String','perguntas: coleção','visão agregada da API'],'projeção'),
node('pergunta','Pergunta',430,130,['id: UUID; posição: int','enunciado: String','alternatives: JSON']),
node('alternativa','Alternativa',820,130,['id: String local à pergunta','texto: String','área: String (servidor)'],'valor JSON'),
node('aluno','Aluno',40,430,['id: UUID','identidade autenticada','resultados restritos ao dono']),
node('resultado','ResultadoPerfil',430,430,['id, user_id: UUID','classificação, resumo: String','answers: JSON; created_at']),
node('resposta','Resposta',820,430,['questionId: UUID','alternativeId: String','valor dentro de answers'],'valor JSON')],edges=[
edge('teste','pergunta',[(280,193),(430,193)],'apresenta',('1','0..*')),
edge('pergunta','alternativa',[(670,193),(820,193)],'contém',('1','0..*')),
edge('aluno','resultado',[(280,493),(430,493)],'obtém',('1','0..*')),
edge('resultado','resposta',[(670,493),(820,493)],'registra',('1','1..*')),
edge('resposta','alternativa',[(940,430),(940,256)],'seleciona',('0..*','1'))],notes=[
'Alternativa.id não é UUID: vale no contexto da pergunta. QuestionId + alternativeId identificam a escolha no questionário.',
'As ligações JSON são lógicas, não chaves estrangeiras; o RPC valida perguntas/alternativas ao submeter o resultado.',
'O questionário vigente reúne e2_profile_questions; não existe tabela teste_perfil nem classe Java TestePerfil.',
'A classificação usa áreas das alternativas no servidor. Finalidade acadêmica/orientativa; não é diagnóstico psicológico.',
'Fontes: Backend/src/app.js (/teste-perfil); Backend/sql/001_entrega2.sql (e2_submit_profile).' ]))
pages.append(dict(title='04 · Classes Java de integração',subtitle='Classes implementadas no Android E2. Recorte das responsabilidades principais; métodos e campos selecionados.',nodes=[
node('hub','HubActivity',40,130,['- answers: Map<String,String>','- adapter: Rows','navega e apresenta recursos'],'classe Java'),
node('api','ApiClient',430,130,['+ request(...): JsonElement','+ publicPost(...): JsonElement','+ parseSession(...): Session'],'classe Java'),
node('service','ApiService',820,130,['+ get(...): Call<JsonElement>','+ post(...): Call<JsonElement>','+ patch(...): Call<JsonElement>'],'interface Retrofit'),
node('repo','AlunoRepository',40,430,['- helper: AlunoDbHelper','+ enqueue(owner, QrPayload)','+ checkins(...): List<Pending>'],'classe Java'),
node('worker','SyncWorker',430,430,['+ doWork(): Result','envia e reconcilia pendências','herda Worker (AndroidX)'],'classe Java'),
node('session','SessionManager',820,430,['+ get(...): Session','+ save(...), clearToken(...)','+ isCurrent(...): boolean'],'classe Java')],edges=[
edge('hub','api',[(280,193),(430,193)],'requisita',kind='dependency'),
edge('api','service',[(670,193),(820,193)],'usa',kind='dependency'),
edge('hub','repo',[(160,256),(160,430)],'consulta',kind='dependency'),
edge('worker','repo',[(430,493),(280,493)],'persiste',kind='dependency'),
edge('worker','api',[(550,430),(550,256)],'requisita',kind='dependency'),
edge('api','session',[(670,230),(745,230),(745,390),(940,390),(940,430)],'renova sessão',kind='dependency')],notes=[
'Seta tracejada = dependência de uso. + público; - privado. Tipos externos: Context, JsonElement, Call e Worker.',
'AlunoRepository possui 1 AlunoDbHelper (SQLiteOpenHelper); classes internas Cached e Pending representam dados locais.',
'QrPayload é imutável (activityId/qrToken finais); parse valida entrada. SyncPolicy.forStatus retorna enum Action.',
'HubActivity/AuthActivity herdam AppCompatActivity. HubActivity.Rows herda RecyclerView.Adapter<Holder> e usa List<Row>.',
'SyncScheduler agenda SyncWorker. SessionManager.Session guarda userId/accessToken/refreshToken/expiresAt (sem valores aqui).',
'Fontes: Frontend/app/src/main/java/com/example/myapplication/{network,data,sync,core} e HubActivity.java.' ]))

pages.append(dict(title='05 · Telas, fragmento e autenticação',subtitle='Classes implementadas no Android E2. Responsabilidade de lifecycle separada da lista de dados.',nodes=[
node('hub','HubActivity',40,130,['- adapter: Rows','+ resourceAdapter(): Adapter','implementa AdapterHost'],'classe Java'),
node('fragment','ResourceListFragment',430,130,['- list: RecyclerView','+ onViewCreated(...)','+ onDestroyView()'],'classe Java'),
node('host','AdapterHost',820,130,['+ resourceAdapter(): Adapter','interface interna de','ResourceListFragment'],'interface Java'),
node('auth','AuthActivity',40,430,['- ATTEMPTS: AuthAttemptGate','login/cadastro assíncronos','invalida callback ao destruir'],'classe Java'),
node('gate','AuthAttemptGate',430,430,['- generation: long','+ begin(), invalidate(ticket)','+ complete(...): boolean'],'classe Java'),
node('input','AuthInputPolicy',820,430,['+ validSignupPassword(...)','retorna boolean','mínimo de 8 caracteres'],'classe Java')],edges=[
edge('fragment','host',[(670,193),(820,193)],'consulta adapter',kind='dependency'),
edge('hub','host',[(160,130),(160,105),(940,105),(940,130)],'',kind='realization'),
edge('auth','gate',[(280,493),(430,493)],'valida tentativa',kind='dependency'),
edge('auth','input',[(160,556),(160,600),(940,600),(940,556)],'valida cadastro',kind='dependency')],notes=[
'Seta tracejada aberta = uso; triângulo vazio tracejado = implementação de interface. Fragmento herda Fragment (AndroidX).',
'FragmentContainerView no XML instancia ResourceListFragment; o host fornece Rows, e o fragmento controla RecyclerView.',
'onDestroyView remove o adapter e limpa a referência da view. AuthAttemptGate descarta callback de tentativa/tela obsoleta.',
'NextMeetingPolicy.next(JsonArray, long): percorre a agenda e escolhe o primeiro horário futuro em America/Sao_Paulo.',
'NextMeetingPolicy não depende do fuso do aparelho; entrada inválida não estabelece um próximo encontro.',
'Fontes: ResourceListFragment.java, HubActivity.java, AuthActivity.java e core/{AuthAttemptGate,AuthInputPolicy,NextMeetingPolicy}.' ]))

pdf=canvas.Canvas(str(ROOT/'UML_Entrega2.pdf'),pagesize=(W,H))
pdf.setTitle('Próxima Etapa - UML e classes implementadas - Entrega 2')
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
        pdf.setFillColor(HexColor('#000000'));pdf.setFont('Helvetica',9);pdf.drawCentredString(x+w/2,H-y-16,'<<'+n['tag']+'>>')
        pdf.setFont('Helvetica-Bold',14);pdf.drawCentredString(x+w/2,H-y-37,n['title']);pdf.line(x,H-y-48,x+w,H-y-48)
        pdf.setFont('Helvetica',11);pdf.setFillColor(HexColor('#000000'))
        for i,line in enumerate(n['lines']):pdf.drawString(x+12,H-y-68-i*19,line)
        val='<<'+n['tag']+'>>\n'+n['title']
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
        c=ET.SubElement(root,'mxCell',id=f'e{i}',value=e['label'],style=style,edge='1',parent='1')
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
    short_notes=[p['notes'][0], 'Detalhes e fontes: GUIA_POO.md e README.md desta pasta. Modelo local da Entrega 2.']
    for i,line in enumerate(short_notes):
        pdf.drawString(40,150-i*18,line)
        c=ET.SubElement(root,'mxCell',id=f'note{i}',value=line,style='text;html=0;align=left;fontSize=10;',vertex='1',parent='1')
        ET.SubElement(c,'mxGeometry',x='40',y=str(H-160+i*18),width='1020',height='20',attrib={'as':'geometry'})
    pdf.setFont('Helvetica',9);pdf.drawString(40,25,'Próxima Etapa - Diagrama de classes - Entrega 2');pdf.drawRightString(W-40,25,f'{page_no} / {len(pages)}')
    pdf.showPage()
pdf.save();ET.indent(mx);ET.ElementTree(mx).write(ROOT/'UML_Entrega2.drawio',encoding='utf-8',xml_declaration=True)
print('PDF e draw.io gerados:',len(pages),'páginas')
