/**
 *
 */
package org.esupportail.ects.web.controllers;

import gouv.education.apogee.commun.transverse.dto.WSReferentiel.recupererSignataire.SignataireWSSignataireDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.esupportail.commons.context.ApplicationContextHolder;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.ects.utils.IExport;
import org.esupportail.ects.web.beans.*;
import org.esupportail.ects.web.beans.pojo.EtudiantPojo;
import org.esupportail.ects.web.beans.pojo.NoteEctsPojo;
import org.jdom.Element;

/**
 * Controller pour le calcul l'echelle de notation ECTS d'une etape.
 * @author gmartel
 *
 */
public class PrintController extends AbstractContextAwareController {

    /*
     ******************* PROPERTIES ******************** */

    /**
     * The serialization id.
     */
    private static final long serialVersionUID = -4273636357306467834L;

    /**
     * Pour l'export des données.
     */
    private IExport export;

    /**
     * Nom de l'établissement sur le relevé de notes
     */
    private String libEtablissement;

    /**
     * fichier xsl 2 Sessions.
     */
//	public static final String XSL_2_SESSIONS = "rvnEcts.xsl";

    /**
     * fichier xsl Session unique.
     */
//	public static final String XSL_SESSION_UNIQUE = "rvnEctsSesUnique.xsl";

    /**
     * fichier xsl Distribution ECTS.
     */
    public static final String XSL_DISTRIB = "distribEcts.xsl";

    /**
     * A logger.
     */
    private final Logger logger = new LoggerImpl(getClass());


    /*
     ******************* INIT ******************** */

    /**
     * Bean constructor.
     */
    public PrintController() {
        super();
    }

    @Override
    public void afterPropertiesSetInternal() {
        super.afterPropertiesSetInternal();
        Assert.notNull(this.export,
            "property export of class " + this.getClass().getName() + " can not be null");
    }

    /*
     ******************* METHODS ******************** */


    public String genereRvn(final List<EtudiantPojo> etudiantsToPrint, final String anneeSelected) {
        String signQuality = null;
        String signName = null;
        Boolean sessionUnique = true;
        if (logger.isDebugEnabled()) {
            logger.debug("entering genereRvn() ");
        }
        try {
            PreferencesController preferencesController = (PreferencesController) ApplicationContextHolder.getContext().getBean("preferencesController");
            String docPdf = "rvnEcts-"
                + etudiantsToPrint.get(0).getVet().getCodEtp()
                + etudiantsToPrint.get(0).getVet().getCodVrsVet()
                + "-" + anneeSelected;
            logger.info("==> Edition du RVN " + docPdf + " par " + getCurrentUser().getId());
            if (etudiantsToPrint.size()==1) {
                docPdf = docPdf + "-"
                    + etudiantsToPrint.get(0).getEtudiant().getCodEtu();
            }
//			Element tabXml;
//			if (preferencesController!=null) {
//				tabXml = toXMLTableau(etudiantsToPrint, anneeSelected, preferencesController.getSignataireSelected());
//			} else {
//				tabXml = toXMLTableau(etudiantsToPrint, anneeSelected, null);
//			}


            JRDataSource jrD = getStudents(etudiantsToPrint);
            String annee = anneeSelected+"/"+(new Integer(anneeSelected) + 1);
            if (preferencesController.getSignataireSelected()!=null) {
                signQuality = preferencesController.getSignataireSelected().getQuaSig();
                signName = preferencesController.getSignataireSelected().getNomSig();
            }

            if (etudiantsToPrint.get(0).getVet().getTemoinSessionUnique().equalsIgnoreCase("N")) {
                sessionUnique = false;
            }

            export.fillManage(jrD, annee, signQuality, signName, libEtablissement, docPdf, FacesContext.getCurrentInstance(), sessionUnique);
//			if (etudiantsToPrint.get(0).getVet().getTemoinSessionUnique().equalsIgnoreCase("N")) {
//				export.exportPdfRvnEcts(tabXml, FacesContext.getCurrentInstance(), XSL_2_SESSIONS, docPdf);
//			} else {
//				export.exportPdfRvnEcts(tabXml, FacesContext.getCurrentInstance(), XSL_SESSION_UNIQUE, docPdf);
//			}
            return null;
        } catch (Exception e) {
            logger.error(e);
        }
        return "go_erreur";
    }

    public JRDataSource getStudents(final List<EtudiantPojo> etudiantsToPrint) {

        //Creation de la collection qui sera renvoyée à Jasper
        List<Student> reportsElement = new ArrayList<Student>();

        for (EtudiantPojo etu : etudiantsToPrint) {

            Student student = new Student();

            student.setNom(etu.getEtudiant().getNom() + " " + etu.getEtudiant().getPrenom());
            student.setNoetu(etu.getEtudiant().getCodEtu());
            student.setIne(etu.getEtudiant().getNumeroIne());
            student.setDatenais(etu.getEtudiant().getDateNaissance());
            student.setEtape(etu.getVet().getLibWebVet());
            student.setCodeEtape(etu.getVet().getCodEtp()+"("+etu.getVet().getCodVrsVet()+")");

            ResultatVet resVet = etu.getRes();
            if(resVet != null) {

                if (resVet.getNote1()!=null) {
                    String noteVet1 = resVet.getBareme1() != 0 ? etu.getRes().getNote1().toString() + " / " + etu.getRes().getBareme1() : etu.getRes().getNote1().toString();
                    student.setNoteVet1(noteVet1);
                }

                String noteEctsVet1 = resVet.getNoteEcts1() != null ? resVet.getNoteEcts1().toString() : null;
                student.setNoteEctsVet1(noteEctsVet1);

                String credits1 = resVet.getCredits1() != null ? resVet.getCredits1().toString() : null;
                student.setCreditsVet1(credits1);

                if (resVet.getNote2()!=null) {
                    String noteVet2 = resVet.getBareme2() != 0 ? etu.getRes().getNote2().toString() + " / " + etu.getRes().getBareme2() : etu.getRes().getNote2().toString();
                    student.setNoteVet2(noteVet2);
                }

                String noteEctsVet2 = resVet.getNoteEcts2() != null ? resVet.getNoteEcts2().toString() : null;
                student.setNoteEctsVet2(noteEctsVet2);

                String credits2 = resVet.getCredits2() != null ? resVet.getCredits2().toString() : null;
                student.setCreditsVet2(credits2);
            }

            List<Elp> elps = new ArrayList<Elp>();

            if (!etu.getElps().isEmpty()) {
                for (ResultatElp etEelp : etu.getElps()) {

                    // Note session 1
                    String note1 = null;
                    if (etEelp.getNote1()!=null) {
                        if (etEelp.getBareme1()!=0) {
                            note1 = etEelp.getNote1().toString() + " / " + etEelp.getBareme1();
                        } else {
                            note1 = etEelp.getNote1().toString();
                        }
                    }

                    String credit1 = null;
                    if (etEelp.getCredits1()!=null) {
                        credit1 = etEelp.getCredits1().toString();
                    }

                    // Note session 2
                    String note2 = null;
                    if (etEelp.getNote2()!=null) {
                        if (etEelp.getBareme2()!=0) {
                            note2 = etEelp.getNote2().toString() + " / " + etEelp.getBareme2();
                        } else {
                            note2 = etEelp.getNote2().toString();
                        }
                    }

                    String credit2 = null;
                    if (etEelp.getCredits2()!=null) {
                        credit2 = etEelp.getCredits2().toString();
                    }
                    elps.add(new Elp(
                        etEelp.getElp().getLibElp(), // UE
                        note1, // Note session 1
                        etEelp.getNoteEcts1(), // Note ECTS session 1
                        credit1, // Credits ECTS session 1
                        note2, // Note session 2
                        etEelp.getNoteEcts2(), // Note ECTS session 2
                        credit2 // Credits ECTS session 2
                    ));
                }
            }

            student.setListElp(elps);

            // ** Légende **
            String leg = "";
            for (String elem : etu.getLegende()) {
                leg = leg + elem + "; ";
            }
            if (!leg.equals("")) {
                student.setLegende(leg);
            }

            reportsElement.add(student);
        }

        return new JRBeanCollectionDataSource(reportsElement);
    }

    /**
     * Permet de recuperer en xml le relevé de notes.
     * @return Element
     *
     */
//	public Element toXMLTableau(final List<EtudiantPojo> etudiantsToPrint
//			, final String anneeSelected, final SignataireWSSignataireDTO signataire) {
//
//		Locale userLocale = getCurrentUserLocale();
//
//		// ** Racine **
//		Element racine = new Element("doc");
//
//		// Pour chaque étudiant
//		for (EtudiantPojo etu : etudiantsToPrint) {
//			// Racine etudiant
//			Element etudiant = new Element("etudiant");
//
//			// ** Entete **
//			Element entete = new Element("entete");
//
//			// Année universitaire
//			Element annee = new Element("annee");
//			Integer annee2 = new Integer(anneeSelected) + 1;
//			annee.setText(anneeSelected+"/"+annee2);
//			entete.addContent(annee);
//
//			Element labelAnnee = new Element("labelAnnee");
//			labelAnnee.setText(getI18nService().getString("ECTS.EDITION.LABEL.ANNEE", userLocale));
//			entete.addContent(labelAnnee);
//
//			// Etablissement
//			Element etab = new Element("etablissement");
//			//etab.setText("Université de Rennes 1");
//			etab.setText(libEtablissement);
//			entete.addContent(etab);
//
//			// Label Relevé de notes
//			Element labelRvn = new Element("labelRvn");
//			labelRvn.setText(getI18nService().getString("ECTS.EDITION.LABEL.RVN", userLocale));
//			entete.addContent(labelRvn);
//
//			etudiant.addContent(entete);
//
//			// Racine rvn
//			Element rvnElement = new Element("rvn");
//
//			// ** Etudiant **
//			Element infosRvn = new Element("infosRvn");
//
//			// Nom et prénom
//			Element nom = new Element("nom");
//			nom.setText(etu.getEtudiant().getNom() + " " + etu.getEtudiant().getPrenom());
//			infosRvn.addContent(nom);
//
//			// No Etudiant
//			Element noetu = new Element("noetu");
//			noetu.setText(etu.getEtudiant().getCodEtu());
//			infosRvn.addContent(noetu);
//
//			Element labelNoetu = new Element("labelnoetu");
//			labelNoetu.setText(getI18nService().getString("ECTS.EDITION.LABEL.NOETU", userLocale));
//			infosRvn.addContent(labelNoetu);
//
//			// INE
//			Element ine = new Element("ine");
//			ine.setText(etu.getEtudiant().getNumeroIne());
//			infosRvn.addContent(ine);
//
//			Element labelIne = new Element("labeline");
//			labelIne.setText(getI18nService().getString("ECTS.EDITION.LABEL.INE", userLocale));
//			infosRvn.addContent(labelIne);
//
//
//			// Date de naissance
//			Element datenais = new Element("datenais");
//			datenais.setText(etu.getEtudiant().getDateNaissance());
//			infosRvn.addContent(datenais);
//
//			Element labelDatenais = new Element("labeldatenais");
//			labelDatenais.setText(getI18nService().getString("ECTS.EDITION.LABEL.DATENAIS", userLocale));
//			infosRvn.addContent(labelDatenais);
//
//
//			// Etape
//			Element etape = new Element("etape");
//			etape.setText(etu.getVet().getLibWebVet());
//			infosRvn.addContent(etape);
//
//			// Resultat VET
//			Element resultat = new Element("resultat");
//			// Note VET session 1
//			Element noteVet1 = new Element("noteVet1");
//			if (etu.getRes().getNote1()!=null) {
//				if (etu.getRes().getBareme1()!=0) {
//					noteVet1.setText(etu.getRes().getNote1().toString() + " / " + etu.getRes().getBareme1());
//				} else {
//					noteVet1.setText(etu.getRes().getNote1().toString());
//				}
//			}
//			resultat.addContent(noteVet1);
//
//			Element labelNoteVet1 = new Element("labelNoteVet1");
//			labelNoteVet1.setText(getI18nService().getString("ECTS.EDITION.LABEL.NOTEVET1", userLocale));
//			resultat.addContent(labelNoteVet1);
//
//
//			// Note ECTS session 1
//			Element noteEctsVet1 = new Element("noteEctsVet1");
//			if (etu.getRes().getNoteEcts1()!=null) {
//				noteEctsVet1.setText(etu.getRes().getNoteEcts1().toString());
//			}
//			resultat.addContent(noteEctsVet1);
//
//			Element labelNoteEctsVet1 = new Element("labelNoteEctsVet1");
//			labelNoteEctsVet1.setText(getI18nService().getString("ECTS.EDITION.LABEL.NOTEECTSVET1", userLocale));
//			resultat.addContent(labelNoteEctsVet1);
//
//			// Credits ECTS session 1
//			Element creditsVet1 = new Element("creditsVet1");
//			if (etu.getRes().getCredits1()!=null) {
//				creditsVet1.setText(etu.getRes().getCredits1().toString());
//			}
//			resultat.addContent(creditsVet1);
//
//			Element labelCreditsVet = new Element("labelCreditsVet");
//			labelCreditsVet.setText(getI18nService().getString("ECTS.EDITION.LABEL.CREDITSVET", userLocale));
//			resultat.addContent(labelCreditsVet);
//
//			// Note VET session 2
//			Element noteVet2 = new Element("noteVet2");
//			if (etu.getRes().getNote2()!=null) {
//				if (etu.getRes().getBareme2()!=0) {
//					noteVet2.setText(etu.getRes().getNote2().toString() + " / " + etu.getRes().getBareme2());
//				} else {
//					noteVet2.setText(etu.getRes().getNote2().toString());
//				}
//			}
//			resultat.addContent(noteVet2);
//
//			Element labelNoteVet2 = new Element("labelNoteVet2");
//			labelNoteVet2.setText(getI18nService().getString("ECTS.EDITION.LABEL.NOTEVET2", userLocale));
//			resultat.addContent(labelNoteVet2);
//
//			// Note ECTS session 2
//			Element noteEctsVet2 = new Element("noteEctsVet2");
//			if (etu.getRes().getNoteEcts2()!=null) {
//				noteEctsVet2.setText(etu.getRes().getNoteEcts2().toString());
//			}
//			resultat.addContent(noteEctsVet2);
//
//			Element labelNoteEctsVet2 = new Element("labelNoteEctsVet2");
//			labelNoteEctsVet2.setText(getI18nService().getString("ECTS.EDITION.LABEL.NOTEECTSVET2", userLocale));
//			resultat.addContent(labelNoteEctsVet2);
//
//			// Credits ECTS session 1
//			Element creditsVet2 = new Element("creditsVet2");
//			if (etu.getRes().getCredits2()!=null) {
//				creditsVet1.setText(etu.getRes().getCredits2().toString());
//			}
//			resultat.addContent(creditsVet2);
//
//			infosRvn.addContent(resultat);
//
//			rvnElement.addContent(infosRvn);
//
//
//			// ** Les éléments pédagogiques **
//			//TODO
//			if (!etu.getElps().isEmpty()) {
//				Element listeElp = new Element("listeElp");
//
//				Element labelUE = new Element("labelUE");
//				labelUE.setText(getI18nService().getString("ECTS.EDITION.LABEL.UE", userLocale));
//				listeElp.addContent(labelUE);
//
//				Element labelSession1 = new Element("labelSession1");
//				labelSession1.setText(getI18nService().getString("ECTS.EDITION.LABEL.SESSION1", userLocale));
//				listeElp.addContent(labelSession1);
//
//				Element labelSession2 = new Element("labelSession2");
//				labelSession2.setText(getI18nService().getString("ECTS.EDITION.LABEL.SESSION2", userLocale));
//				listeElp.addContent(labelSession2);
//
//				Element labelCredits = new Element("labelCredits");
//				labelCredits.setText(getI18nService().getString("ECTS.EDITION.LABEL.CREDITSELP", userLocale));
//				listeElp.addContent(labelCredits);
//
//				Element labelCreditsAcquis = new Element("labelCreditsAcquis");
//				labelCreditsAcquis.setText(getI18nService().getString("ECTS.EDITION.LABEL.ACQUIREDCREDITSELP", userLocale));
//				listeElp.addContent(labelCreditsAcquis);
//
//				Element labelNoteElp = new Element("labelNoteElp");
//				labelNoteElp.setText(getI18nService().getString("ECTS.EDITION.LABEL.NOTEELP", userLocale));
//				listeElp.addContent(labelNoteElp);
//
//				Element labelNoteEctsElp = new Element("labelNoteEctsElp");
//				labelNoteEctsElp.setText(getI18nService().getString("ECTS.EDITION.LABEL.NOTEECTSELP", userLocale));
//				listeElp.addContent(labelNoteEctsElp);
//
//				for (ResultatElp elp : etu.getElps()) {
//					Element ligneElp = new Element("elp");
//
//					// UE
//					Element ue = new Element("ue");
//					ue.setText(elp.getElp().getLibElp());
//					ligneElp.addContent(ue);
//
//					// Note session 1
//					Element note1 = new Element("note1");
//					if (elp.getNote1()!=null) {
//						if (elp.getBareme1()!=0) {
//							note1.setText(elp.getNote1().toString() + " / " + elp.getBareme1());
//						} else {
//							note1.setText(elp.getNote1().toString());
//						}
//					}
//					ligneElp.addContent(note1);
//
//					// Note ECTS session 1
//					Element noteEcts1 = new Element("noteEcts1");
//					if (elp.getNoteEcts1()!=null) {
//						noteEcts1.setText(elp.getNoteEcts1().toString());
//					}
//					ligneElp.addContent(noteEcts1);
//
//					// Credits ECTS session 1
//					Element credits1 = new Element("credits1");
//					if (elp.getCredits1()!=null) {
//						credits1.setText(elp.getCredits1().toString());
//					}
//					ligneElp.addContent(credits1);
//
//					// Note session 2
//					Element note2 = new Element("note2");
//					if (elp.getNote2()!=null) {
//						if (elp.getBareme2()!=0) {
//							note2.setText(elp.getNote2().toString() + " / " + elp.getBareme2());
//						} else {
//							note2.setText(elp.getNote2().toString());
//						}
//					}
//					ligneElp.addContent(note2);
//
//					// Note ECTS session 2
//					Element noteEcts2 = new Element("noteEcts2");
//					if (elp.getNoteEcts2()!=null) {
//						noteEcts2.setText(elp.getNoteEcts2().toString());
//					}
//					ligneElp.addContent(noteEcts2);
//
//					// Credits ECTS session 1
//					Element credits2 = new Element("credits2");
//					if (elp.getCredits2()!=null) {
//						credits1.setText(elp.getCredits2().toString());
//					}
//					ligneElp.addContent(credits2);
//
//
//					listeElp.addContent(ligneElp);
//				}
//				// ** Légende **
//				Element legende = new Element("legende");
//				String leg = "";
//				for (String elem : etu.getLegende()) {
//					leg = leg + elem + "; ";
//				}
//				if (!leg.equals("")) {
//					legende.setText(leg);
//					listeElp.addContent(legende);
//				}
//
//				// ** Signataire **
//				Element signQuality = new Element("qualiteSignataire");
//				Element signName = new Element("nomSignataire");
//				if (signataire!=null) {
//					signQuality.setText(signataire.getQuaSig());
//					signName.setText(signataire.getNomSig());
//
//					listeElp.addContent(signQuality);
//					listeElp.addContent(signName);
//				}
//
//				rvnElement.addContent(listeElp);
//			}
//
//			etudiant.addContent(rvnElement);
//
//			// ** Code Version Etape (en pied de page) **
//			Element codeetape = new Element("codeetape");
//			codeetape.setText(etu.getVet().getCodEtp()+"("+etu.getVet().getCodVrsVet()+")");
//
//			etudiant.addContent(codeetape);
//
//			// ** Date d'édition (en pied de page) **
//			Element dateedit = new Element("dateedit");
//			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//			dateedit.setText(format.format(new Date()));
//
//			etudiant.addContent(dateedit);
//
//			racine.addContent(etudiant);
//
//		}
//
//		return racine;
//	}


    public String genereDistrib(final CalculVet calculVet, final List<CalculElp> calculElps, final String anneeSelected) {
        if (logger.isDebugEnabled()) {
            logger.debug("entering genereDistrib() ");
        }
        try {

            String docPdf = "distribEcts-"
                + calculVet.getVet().getCodEtp()
                + calculVet.getVet().getCodVrsVet()
                + "-" + anneeSelected;
            logger.info("==> Edition de la distrib " + docPdf + " par " + getCurrentUser().getId());

            // TODO

            Element tabXml;
            tabXml = toXMLDistrib(calculVet, calculElps, anneeSelected);
            export.exportPdfDistribEcts(tabXml, FacesContext.getCurrentInstance(), XSL_DISTRIB, docPdf);

            return null;
        } catch (Exception e) {
            logger.error(e);
        }
        return "go_erreur";
    }


    /**
     * Permet de recuperer en xml la distribution d'une VET.
     * @return Element
     *
     */
    public Element toXMLDistrib(final CalculVet calculVet
        , final List<CalculElp> calculElps
        , final String anneeSelected) {

        // ** Racine **
        Element racine = new Element("doc");

        // ** Entete **
        Element entete = new Element("entete");

        // Année universitaire
        Element annee = new Element("annee");
        Integer annee2 = new Integer(anneeSelected) + 1;
        annee.setText(anneeSelected+"/"+annee2);
        entete.addContent(annee);

        racine.addContent(entete);

        // VET
        Element vet = new Element("vet");
        if (calculVet!=null) {

            Element libvet = new Element("libvet");
            libvet.setText(calculVet.getVet().getCodEtp()
                + " : " + calculVet.getVet().getLibWebVet());
            vet.addContent(libvet);

            Element listeNotesVet = new Element("listeNotesVet");

            List<NoteEctsPojo> lnotesvet = calculVet.getEchelleNotation();
            if (lnotesvet!=null) {
                for (NoteEctsPojo note: lnotesvet) {

                    if (note!=null) {
                        if (note.getCodeDistribution()!=null) {

                            Element ligneNote = new Element("noteEcts" + note.getCodeDistribution().name());

                            Element noteMini = new Element("noteMini");
                            if (note.getNoteMini()!=null) {
                                noteMini.setText(note.getNoteMini().toString());
                            }
                            ligneNote.addContent(noteMini);

                            Element noteMaxi = new Element("noteMaxi");
                            if (note.getNoteMaxi()!=null) {
                                noteMaxi.setText(note.getNoteMaxi().toString());
                            }
                            ligneNote.addContent(noteMaxi);

                            listeNotesVet.addContent(ligneNote);
                        }
                    }

                }
                vet.addContent(listeNotesVet);

                racine.addContent(vet);
            }
        }






        Element listeElp = new Element("listeElp");

        // ** Elements pédagogiques **

        // Pour chaque élément
        for (CalculElp elp : calculElps) {

            Element ligneElp = new Element("elp");

            // UE
            Element ue = new Element("ue");
            if (elp.getElp()!=null) {
                ue.setText(elp.getElp().getCodElp()
                    + " : " + elp.getElp().getLibElp());
            }
            ligneElp.addContent(ue);

            Element listeNotes = new Element("listeNotes");

            List<NoteEctsPojo> lnotes = elp.getEchelleNotation();
            if (lnotes!=null) {
                for (NoteEctsPojo note: lnotes) {

                    if (note!=null) {
                        if (note.getCodeDistribution()!=null) {

                            Element ligneNote = new Element("noteEcts" + note.getCodeDistribution().name());

                            Element noteMini = new Element("noteMini");
                            if (note.getNoteMini()!=null) {
                                noteMini.setText(note.getNoteMini().toString());
                            }
                            ligneNote.addContent(noteMini);

                            Element noteMaxi = new Element("noteMaxi");
                            if (note.getNoteMaxi()!=null) {
                                noteMaxi.setText(note.getNoteMaxi().toString());
                            }
                            ligneNote.addContent(noteMaxi);

                            listeNotes.addContent(ligneNote);
                        }
                    }

                }
                ligneElp.addContent(listeNotes);

                listeElp.addContent(ligneElp);
            }
        }

        racine.addContent(listeElp);

        return racine;

    }

    /*
     ******************* ACCESSORS ******************** */


    /**
     * @param libEtablissement the libEtablissement to set
     */
    public void setLibEtablissement(String libEtablissement) {
        this.libEtablissement = libEtablissement;
    }

    /**
     * @return the export
     */
    public IExport getExport() {
        return export;
    }

    /**
     * @param export the export to set
     */
    public void setExport(IExport export) {
        this.export = export;
    }

}
