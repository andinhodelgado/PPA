package br.com.usinasantafe.ppa.control;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.usinasantafe.ppa.model.bean.estaticas.OrdCarregBean;
import br.com.usinasantafe.ppa.model.dao.FuncDAO;
import br.com.usinasantafe.ppa.util.AtualDadosServ;
import br.com.usinasantafe.ppa.model.bean.variaveis.CabPesagemBean;
import br.com.usinasantafe.ppa.model.bean.variaveis.ItemPesagemBean;
import br.com.usinasantafe.ppa.model.dao.CabPesagemDAO;
import br.com.usinasantafe.ppa.model.dao.ItemPesagemDAO;
import br.com.usinasantafe.ppa.model.dao.OrdCarregDAO;
import br.com.usinasantafe.ppa.util.EnvioDadosServ;
import br.com.usinasantafe.ppa.util.Tempo;

public class PesagemCTR {

    private ItemPesagemBean itemPesagemBean;

    public PesagemCTR() {
    }

    ///////////////////////////////// VERIFICAR DADOS ////////////////////////////////////////////

    public boolean hasElementsFunc(){
        FuncDAO funcDAO = new FuncDAO();
        return funcDAO.hasElements();
    }

    public boolean verCabecPesAberto(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        return cabPesagemDAO.verCabecPesAberto();
    }

    public boolean verStatusConCabecPes(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        if(cabPesagemDAO.getCabPesApont().getStatusConCabPes() == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean verOrdCarreg(String placa){
        OrdCarregDAO ordCarregDAO = new OrdCarregDAO();
        return ordCarregDAO.verOrdCarreg(placa);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////// SALVAR/ATUALIZAR/EXCLUIR DADOS /////////////////////////////////

    public void criarCabecPes(String placaVeicCabPes, Long statusCon){
        ConfigCTR configCTR = new ConfigCTR();
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        cabPesagemDAO.criarCabPesagem(placaVeicCabPes, configCTR.getConfig().getIdEquipConfig(), configCTR.getConfig().getMatricFuncConfig(), statusCon);
    }

    public void insItemPes(Double peso, String comentario, Double latitude, Double logitude){
        itemPesagemBean.setPesoItemPes(peso);
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        ItemPesagemDAO itemPesagemDAO = new ItemPesagemDAO();
        itemPesagemDAO.criarItemPesagem(cabPesagemDAO.getCabPesApont().getIdCabPes(), itemPesagemBean, comentario, latitude, logitude);
    }

    public void insItemPes(String comentario, Double latitude, Double logitude){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        ItemPesagemDAO itemPesagemDAO = new ItemPesagemDAO();
        itemPesagemDAO.criarItemPesagem(cabPesagemDAO.getCabPesApont().getIdCabPes(), itemPesagemBean, comentario, latitude, logitude);
    }

    public void fechCabPesagem(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        cabPesagemDAO.fecharCabPesagem();
    }

    public boolean verEnvioDados() {
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        return cabPesagemDAO.verCabPesFechado();
    }

    public void verPlacaVeicServ(String dado, Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        OrdCarregDAO ordCarregDAO = new OrdCarregDAO();
        ordCarregDAO.verDados(dado, telaAtual, telaProx, progressDialog);
    }

    public boolean verStatusConPlacaVeic(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        return cabPesagemDAO.verStatusConPlacaVeic();
    }

    public void deleteDataDif(){
        OrdCarregDAO ordCarregDAO = new OrdCarregDAO();
        ordCarregDAO.deleteDataDif(Tempo.getInstance().dataSHora());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////// GET DADOS /////////////////////////////////////////////

//    public List<CabPesagemBean> cabPesagemApontList(){
//        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
//        return cabPesagemDAO.cabPesagApontList();
//    }

    public List<ItemPesagemBean> itemPesagemApontList(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        List<CabPesagemBean> cabPesagemList = cabPesagemDAO.cabPesagApontList();
        CabPesagemBean cabPesagemBean = cabPesagemList.get(0);
        ItemPesagemDAO itemPesagemDAO = new ItemPesagemDAO();
        List<ItemPesagemBean> itemPesagemList = itemPesagemDAO.getListItemCabec(cabPesagemBean.getIdCabPes());
        cabPesagemList.clear();
        return itemPesagemList;
    }

    public ItemPesagemBean getItemPesagemBean() {
        return itemPesagemBean;
    }

    public List<CabPesagemBean> cabPesagemAbertoList(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        return cabPesagemDAO.cabPesagAbertList();
    }

    public OrdCarregBean getOrdCarregProd(String codProd){
        OrdCarregDAO ordCarregDAO = new OrdCarregDAO();
        return ordCarregDAO.getOrdCarregProd(codProd);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////// SET DADOS /////////////////////////////////////////////

    public void setItemPesagemBean() {
        this.itemPesagemBean = new ItemPesagemBean();
    }

    public void setStatusApontCabPes(CabPesagemBean cabPesagemBean){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        cabPesagemDAO.setStatusApontCabPes(cabPesagemBean);
    }

    public List<OrdCarregBean> osList(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        OrdCarregDAO ordCarregDAO = new OrdCarregDAO();
        return ordCarregDAO.ordCarregList(cabPesagemDAO.getCabPesApont().getPlacaVeicCabPes());
    }

    public List<OrdCarregBean> produtoList(){
        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        OrdCarregDAO ordCarregDAO = new OrdCarregDAO();
        return ordCarregDAO.ordCarregList(cabPesagemDAO.getCabPesApont().getPlacaVeicCabPes(), itemPesagemBean.getNroOSItemPes());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////// VERIFICAÇÃO E ATUALIZAÇÃO DE DADOS POR SERVIDOR /////////////////////

    public void atualDadosFunc(Context telaAtual, Class telaProx, ProgressDialog progressDialog){
        ArrayList colabArrayList = new ArrayList();
        colabArrayList.add("FuncBean");
        AtualDadosServ.getInstance().atualGenericoBD(telaAtual, telaProx, progressDialog, colabArrayList);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////// RECEBER DADOS SERVIDOR ///////////////////////////////////////

    public void deleteCabec(String retorno) {

        try{

            int pos1 = retorno.indexOf("_") + 1;
            String objPrinc = retorno.substring(pos1);

            JSONObject cabecJsonObject = new JSONObject(objPrinc);
            JSONArray cabecJsonArray = cabecJsonObject.getJSONArray("cabec");

            for (int i = 0; i < cabecJsonArray.length(); i++) {

                JSONObject objCabPesagem = cabecJsonArray.getJSONObject(i);
                Gson gsonBol = new Gson();
                CabPesagemBean cabPesagemBean = gsonBol.fromJson(objCabPesagem.toString(), CabPesagemBean.class);

                ItemPesagemDAO itemPesagemDAO = new ItemPesagemDAO();
                itemPesagemDAO.delItemCabec(cabPesagemBean.getIdCabPes());

                CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
                cabPesagemDAO.delCabec(cabPesagemBean.getIdCabPes());

            }

        }
        catch(Exception e){
            EnvioDadosServ.getInstance().setEnviando(false);
        }

    }

    public void deleteCabecAberto() {

        CabPesagemDAO cabPesagemDAO = new CabPesagemDAO();
        CabPesagemBean cabPesagemBean = cabPesagemDAO.getCabPesApont();

        ItemPesagemDAO itemPesagemDAO = new ItemPesagemDAO();
        itemPesagemDAO.delItemCabec(cabPesagemBean.getIdCabPes());

        cabPesagemDAO.delCabec(cabPesagemBean.getIdCabPes());

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////// ENVIO DADOS SERVIDOR ///////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////////////////////////

}
