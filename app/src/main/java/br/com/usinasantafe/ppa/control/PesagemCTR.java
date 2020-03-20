package br.com.usinasantafe.ppa.control;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.usinasantafe.ppa.DigOSActivity;
import br.com.usinasantafe.ppa.DigPlacaVeicActivity;
import br.com.usinasantafe.ppa.model.bean.variaveis.CabPesagemBean;
import br.com.usinasantafe.ppa.model.bean.variaveis.ItemPesagemBean;
import br.com.usinasantafe.ppa.model.dao.CabPesagemDAO;
import br.com.usinasantafe.ppa.model.dao.ItemPesagemDAO;
import br.com.usinasantafe.ppa.model.dao.OSDAO;
import br.com.usinasantafe.ppa.model.dao.VeiculoDAO;
import br.com.usinasantafe.ppa.util.EnvioDadosServ;
import br.com.usinasantafe.ppa.util.Imagem;

public class PesagemCTR {

    private ItemPesagemBean itemPesagemBean;

    public PesagemCTR() {
        if (itemPesagemBean == null)
            itemPesagemBean = new ItemPesagemBean();
    }

    public void criarCabecPes(String placaVeicCabPes, Long statusCon){
        ConfigCTR configCTR = new ConfigCTR();
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        cabPesagemDAO.criarCabPesagem(placaVeicCabPes, configCTR.getConfig().getMatricFuncConfig(), statusCon);
    }

    public boolean verCabecPesAberto(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        return cabPesagemDAO.verCabecPesAberto();
    }

    public void insItemPes(Double peso, String comentario, Double latitude, Double logitude){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        ItemPesagemDAO itemPesagemDAO = new ItemPesagemDAO();
        itemPesagemDAO.criarItemPesagem(cabPesagemDAO.getCabPesAberto().getIdCabPes(), itemPesagemBean, peso, comentario, latitude, logitude);
    }
    public void fechCabPesagem(Bitmap bitmap){
        Imagem imagem = new Imagem();
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        cabPesagemDAO.fechCabPesagem(imagem.getBitmapString(bitmap));
    }

    public String dadosCabecFechEnvio() {
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        CabPesagemBean cabPesagemBean = cabPesagemDAO.getCabPesFechado();
        JsonArray jsonArrayCabec = new JsonArray();
        Gson gsonCabec = new Gson();
        jsonArrayCabec.add(gsonCabec.toJsonTree(cabPesagemBean, cabPesagemBean.getClass()));
        JsonObject jsonCabec = new JsonObject();
        jsonCabec.add("cabec", jsonArrayCabec);
        return jsonCabec.toString();
    }

    public String dadosItemFechEnvio() {
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        ItemPesagemDAO itemPesagemDAO = new ItemPesagemDAO();
        List itemAbordList = itemPesagemDAO.getListItemCabec(cabPesagemDAO.getCabPesFechado().getIdCabPes());

        JsonArray jsonArrayItem = new JsonArray();
        for (int i = 0; i < itemAbordList.size(); i++) {
            ItemPesagemBean itemPesagemBean = (ItemPesagemBean) itemAbordList.get(i);
            Gson gsonItem = new Gson();
            jsonArrayItem.add(gsonItem.toJsonTree(itemPesagemBean, itemPesagemBean.getClass()));
        }

        JsonObject jsonItem = new JsonObject();
        jsonItem.add("item", jsonArrayItem);
        return jsonItem.toString();
    }

    public boolean verEnvioDados() {
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        return cabPesagemDAO.verCabPesFechado();
    }

    public void deleteCabec(String retorno) {

        try{

            int pos1 = retorno.indexOf("_") + 1;
            String objPrinc = retorno.substring(pos1);

            JSONObject cabecJsonObject = new JSONObject(objPrinc);
            JSONArray cabecJsonArray = cabecJsonObject.getJSONArray("cabec");

            for (int i = 0; i < cabecJsonArray.length(); i++) {

                JSONObject objBol = cabecJsonArray.getJSONObject(i);
                Gson gsonBol = new Gson();
                CabPesagemBean cabPesagemBean = gsonBol.fromJson(objBol.toString(), CabPesagemBean.class);

                List cabecList = cabPesagemBean.get("idCabPes", cabPesagemBean.getIdCabPes());
                CabPesagemBean cabPesagemBeanDB = (CabPesagemBean) cabecList.get(0);
                cabecList.clear();

                ItemPesagemBean itemPesagemBean = new ItemPesagemBean();
                List itemPesagemList = itemPesagemBean.get("idCabItemPes", cabPesagemBeanDB.getIdCabPes());

                for (int j = 0; j < itemPesagemList.size(); j++) {

                    itemPesagemBean = (ItemPesagemBean) itemPesagemList.get(j);
                    itemPesagemBean.delete();

                }

                itemPesagemList.clear();
                cabPesagemBeanDB.delete();

            }

        }
        catch(Exception e){
            EnvioDadosServ.getInstance().setEnviando(false);
        }

    }

    public void verPlacaVeicServ(String dado, DigPlacaVeicActivity digPlacaVeicActivity){
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        veiculoDAO.verDados(dado, digPlacaVeicActivity);
    }

    public boolean verPlacaVeicBD(String placa){
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        return veiculoDAO.verBD(placa);
    }

    public void verOSServ(String dado, DigOSActivity digOSActivity){
        OSDAO osDAO = new OSDAO();
        osDAO.verDados(dado, digOSActivity);
    }

    public boolean verOSBD(String placa){
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        return veiculoDAO.verBD(placa);
    }

    public boolean verStatusConPlacaVeic(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        return cabPesagemDAO.verStatusConPlacaVeic();
    }

    public ItemPesagemBean getItemPesagemBean() {
        return itemPesagemBean;
    }

    public Long getStatusConVeicCabPes(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        return cabPesagemDAO.getStatusConVeicCabPes();
    }

    public void deleteCabecAberto() {

        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        CabPesagemBean cabPesagemBean = cabPesagemDAO.getCabPesAberto();

        ItemPesagemBean itemPesagemBean = new ItemPesagemBean();
        List itemPesagemList = itemPesagemBean.get("idCabItemPes", cabPesagemBean.getIdCabPes());

        for (int j = 0; j < itemPesagemList.size(); j++) {

            itemPesagemBean = (ItemPesagemBean) itemPesagemList.get(j);
            itemPesagemBean.delete();

        }

        itemPesagemList.clear();
        cabPesagemBean.delete();

    }

}
