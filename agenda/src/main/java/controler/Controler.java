package controler;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controler", "/main", "/insert", "/select", "/update", "/delete", "/report" })
public class Controler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controler() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		// teste de conexao
		dao.testeConexao();
		String action = request.getServletPath();
		System.out.println(action);
		if (action.equals("/main")) {
			contatos(request, response);
		} else if (action.equals("/insert")) {
			novoContato(request, response);
		} else if (action.equals("/select")) {
			listarContato(request, response);
		} else if (action.equals("/update")) {
			editarContato(request, response);
		} else if (action.equals("/delete")) {
			removerContato(request, response);
		} else if (action.equals("/report")) {
			gerarRelatorio(request, response);
		} else {
			response.sendRedirect("/index.html");

		}
	}

//Listar contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.sendRedirect("agenda.jsp");
		// Criando um objeto que irá receber os dados JavaBeans
		ArrayList<JavaBeans> lista = dao.listarContatos();
		// Encaminhar a lista ao documento agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);

//	    Teste de recebimento da Lista 	
//		for (int i = 0; i < lista.size(); i++) {
//			System.out.println(lista.get(i).getIdcon());
//			System.out.println(lista.get(i).getNome());
//			System.out.println(lista.get(i).getFone());
//			System.out.println(lista.get(i).getEmail());
//			}

	}

	// Novo contato
	protected void novoContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Teste de recebimento dos dados do formulario
//		System.out.println(request.getParameter("nome"));
//		System.out.println(request.getParameter("fone"));
//		System.out.println(request.getParameter("email"));
		// Setar as variaveis JavaBeans
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		// Invocar o método inserirContato passando o objeto contato
		dao.inserirContato(contato);
		// Redirecionar para o documento agenda.jsp
		response.sendRedirect("main");

	}

	// Editar contato
	protected void listarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Recebimento do contato que será editado
		String idcon = request.getParameter("idcon");
// Verificando no console se o server let esta recebendo a requisição		
//		System.out.println(idcon);
		contato.setIdcon(idcon);
		// Executar metodo selecionarContato(DAO)
		dao.selecionarContato(contato);
		// Teste de recebimento que exibe no console
//		System.out.println(contato.getIdcon());
//		System.out.println(contato.getNome());
//		System.out.println(contato.getFone());
//		System.out.println(contato.getEmail());
		// Setar os atributos do formulário com o conteudo Javabeans
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());
		// Encaminhar ao documento editar.jsp
		RequestDispatcher rd = request.getRequestDispatcher("editar.jsp");
		rd.forward(request, response);
	}

	// Alterar um contato
	protected void editarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Teste de recebimento que exibe no console
//		System.out.println(request.getParameter("idcon"));
//		System.out.println(request.getParameter("nome"));
//		System.out.println(request.getParameter("fone"));
//		System.out.println(request.getParameter("email"));
		// Setar a variáveis Javabeans
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		// executa o metodo alterarContato
		dao.alterarContato(contato);
		// redirecionar para o documento agenda.jsp(atualizando as alterações)
		response.sendRedirect("main");
	}

	// Remover um contato
	protected void removerContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Recebimento do id do contato a ser excluido (validador.js)

		String idcon = request.getParameter("idcon");
		// Teste de recebimento que exibe no console
//		System.out.println(idcon);
		contato.setIdcon(idcon);
		// Executar o metordo deletarContato(DAO) passando o objeto contato
		dao.deletarContato(contato);
		// redirecionar para o documento agenda.jsp(atualizando as alterações)
		response.sendRedirect("main");
	}

	// Gerar relatório em PDF
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Document documento = new Document(PageSize.A4);
		documento.setMargins(30f, 30f, 20f, 20f);
		try {
			// tipo de conteúdo
			response.setContentType("apllication/pdf");
			// nome do documento
			response.addHeader("Content-Disposition", "inline; filename=" + "contatos.pdf");
			// criar documento
			PdfWriter.getInstance(documento, response.getOutputStream());
			// abrir documento -- conteudo
			documento.open();
					
			//Adicionando uma imagem
//			@SuppressWarnings("unused")
			Image imagem = Image.getInstance("C:\\Users\\professor\\Pictures\\logo.png");
			imagem.scaleAbsolute(64f, 74f);
//			imagem.setAlignment(Element.ALIGN_CENTER);
//			documento.add(new Paragraph(" "));
//			documento.add(new Paragraph("imagem"));
			documento.add(imagem);
		
			
			documento.add(new Paragraph(" ")); // QUebra uma linha no documento
			documento.add(new Paragraph("Lista de Contatos:"));
			documento.add(new Paragraph(" ")); // QUebra uma linha no documento
			

			// Criar uma tabela
			PdfPTable tabela = new PdfPTable(3);
			tabela.setWidthPercentage(100f);
			tabela.setWidths(new float[] {43f, 23f, 33f});
			// Cabeçalho
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12F)));
			col1.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12F)));
			col2.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell col3 = new PdfPCell(new Paragraph("Email", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12F)));
			col3.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			
			// Popular tabela
			ArrayList<JavaBeans> lista = dao.listarContatos();
			for (int i = 0; i < lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
//			col2.setHorizontalAlignment(Element.ALIGN_CENTER); //deveria centralizar o coteudo da celula
			
			documento.add(tabela);
			//Abrindo o documento pdf com app existente na maquina
//			Runtime.getRuntime().exec(new String[] {"cmd.exe", "/c", "start", "contatos.pdf"});
			documento.close();
			
		} catch (Exception e) {
			System.out.println(e);
			documento.close();
		}
	}
}