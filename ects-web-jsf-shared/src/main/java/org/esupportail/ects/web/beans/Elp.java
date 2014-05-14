package org.esupportail.ects.web.beans;


/**
 * Created by nhenry on 28/04/14.
 */
public class Elp {

    String ue, note1, noteEcts1, credits1, note2, noteEcts2, credits2;

    public Elp(String ue, String note1, String noteEcts1, String credits1, String note2, String noteEcts2, String credits2) {
        this.ue = ue;
        this.note1 = note1;
        this.noteEcts1 = noteEcts1;
        this.credits1 = credits1;
        this.note2 = note2;
        this.noteEcts2 = noteEcts2;
        this.credits2 = credits2;
    }

    public String getUe() {
        return ue;
    }

    public void setUe(String ue) {
        this.ue = ue;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getNoteEcts1() {
        return noteEcts1;
    }

    public void setNoteEcts1(String noteEcts1) {
        this.noteEcts1 = noteEcts1;
    }

    public String getCredits1() {
        return credits1;
    }

    public void setCredits1(String credits1) {
        this.credits1 = credits1;
    }

    public String getNote2() {
        return note2;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }

    public String getNoteEcts2() {
        return noteEcts2;
    }

    public void setNoteEcts2(String noteEcts2) {
        this.noteEcts2 = noteEcts2;
    }

    public String getCredits2() {
        return credits2;
    }

    public void setCredits2(String credits2) {
        this.credits2 = credits2;
    }

}
