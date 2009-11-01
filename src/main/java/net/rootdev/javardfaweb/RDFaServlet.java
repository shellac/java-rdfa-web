/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.rootdev.javardfaweb;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.rootdev.javardfa.NTripleSink;
import net.rootdev.javardfa.ParserFactory;
import net.rootdev.javardfa.ParserFactory.Format;
import net.rootdev.javardfa.RDFXMLSink;
import net.rootdev.javardfa.StatementSink;
import net.rootdev.javardfa.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author pldms
 */
public class RDFaServlet extends HttpServlet {

    final static Logger log = LoggerFactory.getLogger(RDFaServlet.class);

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String target = request.getParameter("uri");
        if (target == null || "".equals(target)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "I need something to parse (try setting 'uri' param)");
            return;
        }
        String type = request.getParameter("parser");

        Format format = (type == null) ? Format.XHTML : Format.lookup(type);
        //response.setCharacterEncoding("US-ASCII");
        response.setCharacterEncoding("UTF-8");
        //response.setContentType("text/plain"); // yeuch
        response.setContentType("application/rdf+xml");
        ServletOutputStream os = response.getOutputStream();
        /*os.print("# Produced by ");
        os.println(Version.get().toString());
        os.print("# From: <");
        os.print(target);
        os.print("> (");
        os.print(format.toString());
        os.println(")");*/
        StatementSink sink = new RDFXMLSink(os);//new NTripleSink(os);
        try {
            XMLReader parser = ParserFactory.createReaderForFormat(sink, format);
            parser.parse(target);
        } catch (SAXException ex) {
            log.error("SAX explosion (target: " + target + " format: " + format + ")" , ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAXplosion");
            return;
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "I only do GET");
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "RDFa parsing thingy";
    }

}
