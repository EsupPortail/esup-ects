package org.esupportail.ects.web.beans;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by nhenry on 28/04/14.
 */
public class Student {

    private List<Elp> listElp = new ArrayList<Elp>();

    private String nom;
    private String noetu;
    private String ine;
    private String datenais;
    private String etape;
    private String codeEtape;

    private String noteVet1;
    private String noteEctsVet1;
    private String creditsVet1;
    private String noteVet2;
    private String noteEctsVet2;
    private String creditsVet2;

    private String legende;

    public static Collection getStudentList()
    {
        List<Student> students = new ArrayList<Student>();
        try
        {
            Student student = new Student();
            students.add(student);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return students;
    }

    public List<Elp> getListElp() {
        return listElp;
    }

    public void setListElp(List<Elp> listElp) {
        this.listElp = listElp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNoetu() {
        return noetu;
    }

    public void setNoetu(String noetu) {
        this.noetu = noetu;
    }

    public String getIne() {
        return ine;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    public String getDatenais() {
        return datenais;
    }

    public void setDatenais(String datenais) {
        this.datenais = datenais;
    }

    public String getEtape() {
        return etape;
    }

    public void setEtape(String etape) {
        this.etape = etape;
    }

    public String getCodeEtape() {
        return codeEtape;
    }

    public void setCodeEtape(String codeEtape) {
        this.codeEtape = codeEtape;
    }

    public String getNoteVet1() {
        return noteVet1;
    }

    public void setNoteVet1(String noteVet1) {
        this.noteVet1 = noteVet1;
    }

    public String getNoteEctsVet1() {
        return noteEctsVet1;
    }

    public void setNoteEctsVet1(String noteEctsVet1) {
        this.noteEctsVet1 = noteEctsVet1;
    }

    public String getCreditsVet1() {
        return creditsVet1;
    }

    public void setCreditsVet1(String creditsVet1) {
        this.creditsVet1 = creditsVet1;
    }

    public String getNoteVet2() {
        return noteVet2;
    }

    public void setNoteVet2(String noteVet2) {
        this.noteVet2 = noteVet2;
    }

    public String getNoteEctsVet2() {
        return noteEctsVet2;
    }

    public void setNoteEctsVet2(String noteEctsVet2) {
        this.noteEctsVet2 = noteEctsVet2;
    }

    public String getCreditsVet2() {
        return creditsVet2;
    }

    public void setCreditsVet2(String creditsVet2) {
        this.creditsVet2 = creditsVet2;
    }

    public String getLegende() {
        return legende;
    }

    public void setLegende(String legende) {
        this.legende = legende;
    }
}
