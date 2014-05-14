/**
 * 
 */
package org.esupportail.ects.utils;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.xml.transform.TransformerException;

import net.sf.jasperreports.engine.JRDataSource;
import org.jdom.Element;


/**
 * Interface contenant les méthodes d'export PDF.
 * @author gmartel
 *
 */
public interface IExport {
	
	/**
	 * Permet de creer un pdf relevé de note ECTS.
	 * @param elfinal
	 * @param facesContext
	 * @param fileName
	 * @throws IOException
	 * @throws TransformerException
	 */
//	void exportPdfRvnEcts(Element elfinal, FacesContext facesContext,
//			final String fileXsl,
//			final String fileName);
	
	
	/**
	 * Permet de creer un pdf distribution ECTS d'une VET.
	 * @param elfinal
	 * @param facesContext
	 * @param fileName
	 * @throws IOException
	 * @throws TransformerException
	 */
	void exportPdfDistribEcts(Element elfinal, FacesContext facesContext, 
			final String fileXsl, 
			final String fileName);

    void fillManage(JRDataSource jrD, String anneeSelected,
                    String quaSig, String nomSig, String libEtab,
                    String fileName, FacesContext facesContext,
                    Boolean sessionUnique);
}
