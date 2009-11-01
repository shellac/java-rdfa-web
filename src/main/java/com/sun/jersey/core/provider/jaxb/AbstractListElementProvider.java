/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.jersey.core.provider.jaxb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Providers;

/**
 * Null implementation
 */
public abstract class AbstractListElementProvider extends AbstractJAXBProvider<Object> {

    public AbstractListElementProvider(Providers ps) {
        super(ps);
    }
    
    public AbstractListElementProvider(Providers ps, MediaType mt) {
        super(ps, mt);        
    }
    
    public boolean isReadable(Class<?> type, Type genericType, Annotation annotations[], MediaType mediaType) {
    	return false;
    }
    
    public boolean isWriteable(Class<?> type, Type genericType, Annotation annotations[], MediaType mediaType) {
    	return false;
    }
    
    private boolean verifyArrayType(Class type) {
    	return false;
    }

    private boolean verifyGenericType(Type genericType) {
    	return false;
    }
    
    public final void writeTo(
            Object t,
            Class<?> type, 
            Type genericType, 
            Annotation annotations[], 
            MediaType mediaType, 
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException {
    }

    /**
     * Write a collection of JAXB objects as child elements of the root element.
     * 
     * @param elementType the element type in the collection.
     * @param t the collecton to marshall
     * @param mediaType the media type
     * @param c the charset
     * @param m the marshaller
     * @param entityStream the output stream to marshall the collection
     * @throws javax.xml.bind.JAXBException
     * @throws IOException 
     */
    public abstract void writeList(Class<?> elementType, Collection<?> t,
            MediaType mediaType, Charset c,
            Object m, OutputStream entityStream)
            throws IOException;
    
    public final Object readFrom(
            Class<Object> type,
            Type genericType, 
            Annotation annotations[],
            MediaType mediaType, 
            MultivaluedMap<String, String> httpHeaders, 
            InputStream entityStream) throws IOException { 
    	return null;
    }

    private Object createArray(List l, Class componentType) {
    	return null;
    }
    
    /**
     * Get the XMLStreamReader for unmarshalling.
     *
     * @param elementType the individual element type.
     * @param mediaType the media type.
     * @param unmarshaller the unmarshaller as a carrier of possible config options.
     * @param entityStream the input stream.
     * @return the XMLStreamReader.
     * @throws javax.xml.stream.XMLStreamException
     */
    protected abstract Object getXMLStreamReader(Class<?> elementType, MediaType mediaType, Object unmarshaller,
            InputStream entityStream);

    protected Class getElementClass(Class<?> type, Type genericType) {
    	return null;
    }
    

    protected final String getRootElementName(Class<?> elementType) {
        return null;
    }    
}