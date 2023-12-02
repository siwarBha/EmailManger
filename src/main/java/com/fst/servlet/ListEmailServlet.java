package com.fst.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet implementation class ListEmailServlet
 */
@WebServlet("/ListEmailServlet")
public class ListEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    ArrayList<String> addresses = new ArrayList<String>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ListEmailServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		ObjectInputStream in;
		try {
			getUsersFromDB();
			System.out.println("Addresses from DB: " + addresses);
			// lecture de fichier des adresses
			// recuperer le nom de fichier de parametre
			// String fileName = getInitParameter("filePath").toString();
			String fileName = "C:\\Users\\user\\eclipseJEE\\EmailManager\\src\\main\\java\\com\\fst\\servlet\\adresses.txt";
			// lire le fichier
			FileInputStream fileIn = new FileInputStream(fileName);
			in = new ObjectInputStream(fileIn);
			in.readObject();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>Liste Email</title>");
	    out.println("<style>");
	    out.println("body { font-family: 'Arial', sans-serif; background-color: #f4f4f4; margin: 20px;}");
	    out.println("ul { list-style-type: none; padding: 0;}");
	    out.println("li { background-color: #fff; padding: 10px; margin-bottom: 5px; border-radius: 5px; box-shadow: 0 0 5px rgba(0,0,0,0.1);}");
	    out.println("form { margin-top: 20px;}");
	    out.println("input[type='text'] { padding: 10px; width: 250px;}");
	    out.println("input[type='submit'] { background-color: #4caf50; color: white; padding: 10px 15px; border: none; border-radius: 5px; cursor: pointer;}");
	    out.println("input[type='submit'][name='unsubscribe'] { background-color: #e74c3c;}");
	    out.println("</style>");
	    out.println("<script>");
	    out.println("function validateEmail() {");
	    out.println("   var emailInput = document.getElementById('email');");
	    out.println("   var emailRegex = /^[\\w.-]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$/;");
	    out.println("   var isValid = emailRegex.test(emailInput.value);");
	    out.println("   if (!isValid) {");
	    out.println("       alert('Veuillez entrer une adresse e-mail valide.');");
	    out.println("       return false;");
	    out.println("   }");
	    out.println("   return true;");
	    out.println("}");
	    out.println("</script>");
	    out.println("</head>");
	    out.println("<body>");
	    out.println("<p><b>Membres:</b></p>");
	    out.println("<ul>");

	    for (int i = 0; i < addresses.size(); i++) {
	        out.println("<li>" + addresses.get(i) + "</li>");
	    }

	    out.println("</ul>");
	    out.println("<FORM METHOD=POST ACTION=ListEmailServlet onsubmit='return validateEmail();'>");
	    out.println("Entrer votre adresse mail: <INPUT TYPE=TEXT NAME=email id='email'><BR><br>");
	    out.println("<INPUT TYPE=submit NAME=action VALUE=unsubscribe style="
	            + "\"background-color:red; mergin-left:150px;width:150px;height:40px;color:white\">");
	    out.println("<INPUT TYPE=submit NAME=action VALUE=subscribe style="
	            + "\"background-color:green;width:150px;height:40px;color:white\">");
	    out.println("</form>");
	    out.println("</body>");
	    out.println("</html>");
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String button = request.getParameter("action");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>List Manager</title></head>");
        out.println("<style>");
        out.println("body { font-family: 'Arial', sans-serif; background-color: #f4f4f4; margin: 20px; text-align: center; }");
        out.println("p { color: #333; }");
        out.println("a { display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #3498db; color: #fff; text-decoration: none; border-radius: 5px; transition: background-color 0.3s; }");
        out.println("a:hover { background-color: #2980b9; }");
        out.println("</style>");
        out.println("<body>");

        if ("subscribe".equals(button)) {
            if (email == null || email.trim().isEmpty()) {
                String msg = "Aucune adresse email spécifiée.";
                out.println("<p style='color: #e74c3c;'>" + msg + "</p>");
            } else {
                subscribe(email);
                out.print("<p>Adresse <b>" + email + "</b> inscrite.</p><br>");
            }
        } else if ("unsubscribe".equals(button)) {
            if (email == null || email.trim().isEmpty()) {
                String msg = "Aucune adresse email spécifiée.";
                out.println("<p style='color: #e74c3c;'>" + msg + "</p>");
            } else if (!addresses.contains(email)) {
                String msg = "Adresse : " + email + " non inscrite.";
                out.println("<p style='color: #e74c3c;'>" + msg + "</p>");
            } else {
                unsubscribe(email);
                out.print("<p>Adresse <b>" + email + "</b> supprimée.</p><br>");
            }
        }

        out.print("<a href=\"ListEmailServlet\">Afficher la liste</a>");
        save();

        out.println("</body>");
        out.println("</html>");
    }
	// ta9ra lfichier b ligne b ligne w tsob f liste
	private void readList() {
		String ligne;
		try {
			// String fileName = getInitParameter("filePath").toString();
			String fileName = "C:\\Users\\user\\eclipseJEE\\EmailManager\\src\\main\\java\\com\\fst\\servlet\\adresses.txt";

			File f = new File(fileName);
			BufferedReader B = new BufferedReader(new FileReader(f));
			while ((ligne = B.readLine()) != null) {
				addresses.add(ligne);
			}
			B.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void save() throws IOException {
		// String fileName = getInitParameter("filePath").toString();
		String fileName = "C:\\Users\\user\\eclipseJEE\\EmailManager\\src\\main\\java\\com\\fst\\servlet\\adresses.txt";

		File f = new File(fileName);
		PrintWriter p = new PrintWriter(f);
		for (int i = 0; i < addresses.size(); i++) {
			p.println(addresses.get(i));
		}
		p.close();

	}

	public void subscribe(String email) {
		addresses.add(email);

	}

	public void unsubscribe(String email) {
		addresses.remove(email);
	}

// liaison avec bd
	public void getUsersFromDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/projet_jee?characterEncoding=utf8", "root", "root");
			PreparedStatement preparedStatement = connection.prepareStatement("select * from users;");
			ResultSet res = preparedStatement.executeQuery();

			while (res.next()) {
                String email = res.getString("Email");
                String pwd = res.getString("pwd");
                String nom = res.getString("Nom");
                String prenom = res.getString("Prenom");
                addresses.add(email);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
	}

}
