package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.database.SmoothieDao;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.Smoothie;
import tikape.runko.domain.SmoothieRaakaAine;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:db/smoothiearkisto.db");
        database.init();
        
        RaakaAineDao raakaAineDao = new RaakaAineDao(database, "RaakaAine");
        SmoothieDao smoothieDao = new SmoothieDao(database, "annos");
        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database, "AnnosRaakaAine");
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            
            map.put("annokset", smoothieDao.findAll());
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/ainekset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("ingredients", raakaAineDao.findAll());
            
            return new ModelAndView(map, "ainekset");
        }, new ThymeleafTemplateEngine());
        
        post("/ainekset", (req, res) -> {
            String action = req.queryParams("nappi");
            switch (action) {
                case "Poista":
                    Integer ingredientId = Integer.parseInt(req.queryParams("ingredientId"));
                    raakaAineDao.delete(ingredientId);
                    break;
                case "Lisaa":
                    RaakaAine rawMat = new RaakaAine(-1, req.queryParams("ingredientName"));
                    raakaAineDao.saveOrUpdate(rawMat);
                    break;
            }
            res.redirect("/ainekset");
            
            return "test";
        });
        
        post("/smoothiet/", (req, res) -> {
            String action = req.queryParams("nappi");
            switch (action) {
                case "addSmoothie":
                    Smoothie tempSmoothie = new Smoothie(-1, req.queryParams("smoothieName"));
                    smoothieDao.saveOrUpdate(tempSmoothie);
                    break;
                case "AddNewIngredients":
                    Integer annos_id = Integer.parseInt(req.queryParams("smoothieId"));
                    Integer raw_id = Integer.parseInt(req.queryParams("ingredientId"));
                    Integer jarj = Integer.parseInt(req.queryParams("Order"));
                    String maara = req.queryParams("Quantity");
                    String ohje = req.queryParams("Info");
                    SmoothieRaakaAine tempKombo = new SmoothieRaakaAine(raw_id, annos_id, jarj, maara, ohje);
                    annosRaakaAineDao.saveOrUpdate(tempKombo);
                    break;
                case "Poista":
                    Integer smoothieDeleteId = Integer.parseInt(req.queryParams("SmoothieDeleteId"));
                    smoothieDao.delete(smoothieDeleteId);
                    break;
                
            }
            
            res.redirect("/smoothiet/");
            return "";
        });
        
        get("/smoothiet/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", smoothieDao.findAll());
            map.put("smoothies", smoothieDao.findAll());
            map.put("ingredients", raakaAineDao.findAll());
            
            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());
        
        get("/smoothiet/:id/", (req, res) -> {
            HashMap map = new HashMap<>();
            System.out.println(Integer.parseInt(req.params("id")));
            map.put("aineet", annosRaakaAineDao.listaaRaakaAineetAnnokselle(Integer.parseInt(req.params("id"))));
            map.put("smoothie", smoothieDao.findOne(Integer.parseInt(req.params("id"))));
            return new ModelAndView(map, "smoothie");
        }, new ThymeleafTemplateEngine());
        
        post("/ainekset/:id/poista", (req, res) -> {
            raakaAineDao.delete(Integer.parseInt(req.params("id")));
            System.out.println("joo poistoon");
            res.redirect("/ainekset");
            return "joo";
        });
        
        get("/ainekset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("ingredient", raakaAineDao.findOne(Integer.parseInt(req.params("id"))));
            
            return new ModelAndView(map, "ingredient");
        }, new ThymeleafTemplateEngine());
    }
}
