/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author petri.nieminen
 */
public class SmoothieRaakaAine {
    
    Integer raaka_aine_id;
    Integer annos_id;
    Integer jarjestys;
    String maara;
    String ohje;

    public SmoothieRaakaAine(Integer raaka_aine_id, Integer annos_id, Integer jarjestys, String maara, String ohje) {
        this.raaka_aine_id = raaka_aine_id;
        this.annos_id = annos_id;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Integer getRaaka_aine_id() {
        return raaka_aine_id;
    }

    public void setRaaka_aine_id(Integer raaka_aine_id) {
        this.raaka_aine_id = raaka_aine_id;
    }

    public Integer getAnnos_id() {
        return annos_id;
    }

    public void setAnnos_id(Integer annos_id) {
        this.annos_id = annos_id;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }

    public String getMaara() {
        return maara;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }
    
    
    
}
