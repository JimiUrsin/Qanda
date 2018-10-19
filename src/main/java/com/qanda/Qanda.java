package main.java.com.qanda;

import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Qanda {

    public static void main(String[] args) {


        Database database = null;
        try {
            database = new Database("jdbc:postgresql://localhost/main");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            System.exit(0);
        }

        KysymysDao kDao = new KysymysDao(database);
        VastausDao vDao = new VastausDao(database);

        Connection conn = null;
        try {
            conn = database.getConnection();
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS Kysymys (id Serial, kurssi Varchar(255), aihe Varchar(255), kysymysteksti Varchar(500), PRIMARY KEY(id));");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS Vastaus (id Serial, kysymys_id Integer, vastausteksti Varchar(500), oikein Boolean, PRIMARY KEY(id), FOREIGN KEY(kysymys_id) REFERENCES Kysymys(id));");
            kDao.delete(-1);

            ArrayList<Kysymys> test = (ArrayList<Kysymys>) kDao.findAll();

            for(Kysymys k : test) {
                System.out.println(String.join(",", "" + k.getId(), k.getAihe(), k.getKurssi(), k.getKysymysteksti()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        Spark.get("/sivu", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kurssit", kDao.createFilteredMap());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/del", (req, res) -> {
            int i;
            i = Integer.parseInt(req.queryParams("id"));

            kDao.delete(i);

            res.redirect("/sivu");
            return "";
        });

        Spark.get("/luo", (req, res) -> {
            String a, b, c;
            a = req.queryParams("kurssi");
            b = req.queryParams("aihe");
            c = req.queryParams("kysymysteksti");

            if (a.isEmpty() || b.isEmpty() || c.isEmpty()) {
                res.redirect("/sivu");
                return "";
            }


            kDao.saveOrUpdate(new Kysymys(-1, a, b, c));
            res.redirect("/sivu");
            return "";
        });

        Spark.get("/vastaus", (request, response) -> {
            int id;
            String teksti;
            boolean totta;
            try {
                id = Integer.parseInt(request.queryParams("id"));
            } catch (Exception e) {
                return "";
            }
            if (request.queryParams("vastausteksti").equals("")) return "";
            teksti = request.queryParams("vastausteksti");
            totta = request.queryParams().contains("totta");


            vDao.saveOrUpdate(new Vastaus(-1, kDao.findOne(id), teksti, totta));

            response.redirect("/" + id);
            return "";
        });

        Spark.get("/delv", ((request, response) -> {
            int id = Integer.parseInt(request.queryParams("id"));
            Vastaus v = vDao.findOne(id);
            if (v == null) return "";
            int returnId = v.getKysymys().getId();
            vDao.delete(id);
            response.redirect("/" + returnId);
            return "";
        }));

        Spark.get("/:id", (request, response) -> {
            int id;
            HashMap map = new HashMap<>();
            try {
                id = Integer.parseInt(request.params("id"));
            } catch(Exception e) {
                response.redirect("/sivu");
                return new ModelAndView(map, "kysymys");
            }
            map.put("vastaukset", vDao.findByKysymysId(id));
            map.put("kysymys", kDao.findOne(id));
            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());

    }
}
