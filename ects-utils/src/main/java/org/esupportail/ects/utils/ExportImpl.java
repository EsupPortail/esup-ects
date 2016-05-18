/**
 * 
 */
package org.esupportail.ects.utils;


import static javax.xml.transform.OutputKeys.ENCODING;
import static org.apache.xmlgraphics.util.MimeConstants.MIME_PDF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.commons.web.servlet.DownloadServlet;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


/**
 * @author gmartel
 *
 */
public class ExportImpl implements IExport {


	/*
	 ******************* PROPERTIES ******************* */

	private static final Logger LOGGER = new LoggerImpl(ExportImpl.class);	

	
	/**
	 * le chemin du repertoire où sont stockés les fichiers xml et xsl nécessaire à l'export. 
	 */
	private String pathXmlXsl;
    private String pathJasperTemplate;


    /**
	 * Constructor. 
	 */
	public ExportImpl() {
		super();
	}

	/**
	 * Est appele par toute methode qui produit du PDF via une feuille xsl et des données XML.
	 * @param xml
	 * @param xsl
	 * @return byte[]
	 * @throws TransformerException 
	 * @throws FOPException 
	 */
	public static byte[] transformXMLPDF(final File xml,
			final File xsl) throws TransformerException, FOPException {
		ByteArrayOutputStream content = new ByteArrayOutputStream();
		
		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		Fop fop = fopFactory.newFop(MIME_PDF, foUserAgent, content);
		
		// Setup XSLT
		Source style = new StreamSource(xsl);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(style);
        transformer.setOutputProperty(ENCODING, "UTF-8");

        // Set the value of a <param> in the stylesheet
        //transformer.setParameter("versionParam", "2.0");

        // Récupération de la source xml
        Source source = new StreamSource(xml);

        // Resulting SAX events (the generated FO) must be piped through to FOP
        Result resultat = new SAXResult(fop.getDefaultHandler());

		// Transformation
		transformer.transform(source, resultat);
		byte[] contentPdf = content.toByteArray();
		
		return contentPdf;
	}

    @Override
	public void fillManage(JRDataSource datasrc, final String anneeSelected, final String quaSig,
                           final String nomSig, final String libEtab,
                           final String fileName, final FacesContext facesContext,
                           final Boolean sessionUnique) {

        String sourceFileName = getPathJasperTemplate() + "report3.jasper";

        Map parameters = new HashMap();
        parameters.put("annee", anneeSelected);
        parameters.put("etablissement", libEtab);
        parameters.put("qualite", quaSig);
        parameters.put("signataire", nomSig);
        parameters.put("sessionUnique", sessionUnique);

        try {
            JasperPrint jp = JasperFillManager.fillReport(sourceFileName, parameters, datasrc);
            if (jp != null) {
                byte[] contentPdf = JasperExportManager.exportReportToPdf(jp);
                setDownLoadAndSend(contentPdf, facesContext,"application/pdf", "attachment", fileName + ".pdf");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

//	/**
//	 * @see org.esupportail.ects.utils.IExport#exportPdfRvnEcts(org.jdom.Element, javax.faces.context.FacesContext, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void exportPdfRvnEcts(final Element el, final FacesContext facesContext,
//			final String fileXsl,
//			final String fileName) {
//		File xml = null;
//		try {
//
//			Document doc = new Document(el);
//			xml = File.createTempFile(fileName, ".xml");
//
//	        Format format =  Format.getCompactFormat().setEncoding("UTF-8");
//	        FileOutputStream output = new FileOutputStream(xml);
//	        XMLOutputter serializer  = new XMLOutputter(format);
//
//	        serializer.output(doc, output);
//	        output.flush();
//	        output.close();
//
//			File xsl = new File(getPathXmlXsl() + fileXsl);
//
//			byte[] contentPdf = transformXMLPDF(xml, xsl);
//
//			setDownLoadAndSend(contentPdf, facesContext,"application/pdf", "attachment", fileName + ".pdf");
//
//			return;
//
//		} catch (Exception e) {
//			LOGGER.error(e);
//		} finally {
//			if (xml != null && xml.exists()) {
//				xml.delete();
//			}
//		}
//		return;
//	}
	
	@Override
	public void exportPdfDistribEcts(Element el, FacesContext facesContext,
			String fileXsl, String fileName) {
		File xml = null;
		try {
			
			Document doc = new Document(el);

			xml = File.createTempFile(fileName, ".xml");
			FileWriter writer = new FileWriter(xml);
			
			XMLOutputter outputter = new XMLOutputter();
			Format format = outputter.getFormat();
			format.setEncoding("UTF-8");
			outputter.setFormat(format);
			outputter.output(doc, writer);
			writer.close();
			
			File xsl = new File(getPathXmlXsl() + fileXsl);
			
			byte[] contentPdf = transformXMLPDF(xml, xsl);
			
			setDownLoadAndSend(contentPdf, facesContext,"application/pdf", "attachment", fileName + ".pdf");
			
			return;
			
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			if (xml != null && xml.exists()) {
				xml.delete();
			}
		}
		return;
		
	}

    /**
     * @param file
     * @param facesContext
     * @param contentType
     */
    public void setDownLoadAndSend(final File file,
                                   final FacesContext facesContext,
                                   final String contentType,
                                   final String contentDisposition) {

        try {
            LOGGER.debug("DownloadUtils.setDownload(data," + contentType + "," + contentDisposition + ")");
            Long id = DownloadUtils.setDownload(file, contentType, contentDisposition);
            String url = getDownloadUrl(id);
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.redirect(url);
            facesContext.responseComplete();
        } catch (IOException e) {
            LOGGER.error("probleme lors de l envoi d un ficher. - exception : " + e);
        }
    }

	/**
	 * @param data
	 * @param facesContext
	 * @param contentType
	 * @param fileName
	 */
	public void setDownLoadAndSend(final byte[] data,
			final FacesContext facesContext, 
			final String contentType, 
			final String contentDisposition, 
			final String fileName) {
		
		try {
			LOGGER.debug("DownloadUtils.setDownload(data," + fileName + "," + contentType + "," + contentDisposition + ")");
			Long id = DownloadUtils.setDownload(data, fileName, contentType, contentDisposition);
			String url = getDownloadUrl(id);
			ExternalContext externalContext = facesContext.getExternalContext();
			externalContext.redirect(url);
			facesContext.responseComplete();
		} catch (IOException e) {
			LOGGER.error("probleme lors de l envoi d un ficher = " + fileName + "exception : " + e);
		}
	}
	
	/**
	 * @param id 
	 * @return the download URL (to redirect to).
	 * @throws DownloadException 
	 */
	private static String getDownloadUrl(
			final long id) throws DownloadException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		String downloadUrl;
		downloadUrl = externalContext.getRequestContextPath() + "/download"; 
		downloadUrl += "?" + DownloadServlet.ID_ATTRIBUTE + "=" + id;
		return downloadUrl;
	}


	/* *************************GETTERS ET SETTERS***************************************************************/
	

	/**
	 * @return the pathXmlXsl
	 */
	public String getPathXmlXsl() {
		return pathXmlXsl;
	}


	/**
	 * @param pathXmlXsl the pathXmlXsl to set
	 */
	public void setPathXmlXsl(final String pathXmlXsl) {
		this.pathXmlXsl = pathXmlXsl;
	}

    /**
     * @return the pathJasperTemplate
     */
    public String getPathJasperTemplate() {
        return pathJasperTemplate;
    }

    /**
     * @param pathJasperTemplate the pathJasperTemplate to set
     */
    public void setPathJasperTemplate(String pathJasperTemplate) {
        this.pathJasperTemplate = pathJasperTemplate;
    }


}
