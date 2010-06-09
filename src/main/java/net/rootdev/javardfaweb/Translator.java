/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.rootdev.javardfaweb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.StreamingOutput;
import net.rootdev.javardfa.ParserFactory;
import net.rootdev.javardfa.ParserFactory.Format;
import net.rootdev.javardfa.StatementSink;
import net.rootdev.javardfa.Version;
import net.rootdev.javardfa.output.NTripleSink;
import net.rootdev.javardfa.output.RDFXMLSink;
import net.rootdev.javardfa.output.TurtleSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * REST Web Service
 *
 * @author pldms
 */

@Path("/")
public class Translator {
    private static final Logger log = LoggerFactory.getLogger(Translator.class);

    //@Context
    //private UriInfo context;
    private final String comment;

    /** Creates a new instance of Translator */
    public Translator() {
        comment = "Produced by " + Version.get();
    }
    
    @GET
    @Produces("application/rdf+xml")
    public StreamingOutput getRDFXML(
            @QueryParam("uri") final URI uri,
            @QueryParam("parser")
            @DefaultValue("XHTML") final Format format) {
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                StatementSink sink = 
                        new RDFXMLSink(output, comment, "Origin: <" + uri + ">");
                parse(uri, format, sink, output);
            }
        };
    }
    
    @GET
    @Produces("application/xml")
    public StreamingOutput getXML(
            @QueryParam("uri") final URI uri,
            @QueryParam("parser")
            @DefaultValue("XHTML") final Format format) {
        return getRDFXML(uri, format);
    }

    @GET
    @Produces("text/plain")
    public StreamingOutput getNTriples(
            @QueryParam("uri") final URI uri,
            @QueryParam("parser")
            @DefaultValue("XHTML") final Format format) {
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                StatementSink sink =
                        new NTripleSink(output, comment, "Origin: <" + uri + ">");
                parse(uri, format, sink, output);
            }
        };
    }

    @GET
    @Produces("application/turtle")
    public StreamingOutput getTurtle(
            @QueryParam("uri") final URI uri,
            @QueryParam("parser")
            @DefaultValue("XHTML") final Format format) {
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                StatementSink sink =
                        new TurtleSink(output, comment, "Origin: <" + uri + ">");
                parse(uri, format, sink, output);
            }
        };
    }

    @POST
    @Consumes("text/html")
    @Produces("text/plain")
    public StreamingOutput handleHTMLContent(final InputStream content) {
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                StatementSink sink =
                        new NTripleSink(output, comment, "Origin: POSTed");
                parse(content, Format.HTML, sink, output);
            }
        };
    }

    @POST
    @Consumes("application/xhtml+xml")
    @Produces("text/plain")
    public StreamingOutput handleXHTMLContent(final InputStream content) {
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                StatementSink sink =
                        new NTripleSink(output, comment, "Origin: POSTed");
                parse(content, Format.XHTML, sink, output);
            }
        };
    }

    protected void parse(InputStream content, Format format, StatementSink sink, OutputStream output) {
        InputSource in = new InputSource(content);
        in.setSystemId("urn:invalid:posted");
        in.setEncoding("utf-8");
        parse(in, format, sink, output);
    }

    protected void parse(URI url, Format format, StatementSink sink, OutputStream output) {
        if (!"http".equals(url.getScheme())) throw new RuntimeException("I only do http");
        InputSource in = new InputSource(url.toString());
        in.setEncoding("utf-8");
        parse(in, format, sink, output);
    }

    protected void parse(InputSource in, Format format, StatementSink sink, OutputStream output) {
        try {
            XMLReader parser = ParserFactory.createReaderForFormat(sink, format);
            parser.parse(in);
        } catch (SAXException ex) {
            midstreamError(ex, output);
        } catch (IOException ex) {
            midstreamError(ex, output);
        }
    }

    private void midstreamError(Exception ex, OutputStream output) {
        PrintStream p = new PrintStream(output);
        p.println("###########################################");
        p.println("######### Mid-stream error ################");
        ex.printStackTrace(p);
        p.println("###########################################");
        try {
            output.flush();
        } catch (IOException e) {
            // What do you expect me to do here??
            log.error("Flushing went wrong", e);
        }
        throw new WebApplicationException(ex);
    }


}
