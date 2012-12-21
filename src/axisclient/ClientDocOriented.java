/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axisclient;

import axisservice.AxisServiceStub;
import java.rmi.RemoteException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Реализация клиента справочника, использующая документо-ориентированное взаимодействие с сервисом
 *  
 * @author Beloglazov Oleg
 */
public class ClientDocOriented implements Client{
    
    AxisServiceStub mService;
    
    AxisServiceStub.GetArticlesId mGetArticlesId;
    AxisServiceStub.GetArticleName mGetArticleName;
    AxisServiceStub.GetArticleContent mGetArticleContent;
    AxisServiceStub.AddArticle mAddArticle;
    AxisServiceStub.RemoveArticle mRemoveArticle;
    AxisServiceStub.SetArticleContent mSetArticleContent;
    
    AxisServiceStub.GetArticlesIdResponse mGetArticlesIdResponse;    
    AxisServiceStub.GetArticleNameResponse mGetArticleNameResponse; 
    AxisServiceStub.GetArticleContentResponse mGetArticleContentResponse;    
    AxisServiceStub.AddArticleResponse mAddArticleResponse;    
    AxisServiceStub.RemoveArticleResponse mRemoveArticleResponse;    
    AxisServiceStub.SetArticleContentResponse mSetArticleContentResponse;
    
    /** Стандартынй конструктор
     *  
     */
    public ClientDocOriented() {
        try {
            mService = new AxisServiceStub();
            
            mGetArticlesId = new AxisServiceStub.GetArticlesId();
            mGetArticleName = new AxisServiceStub.GetArticleName();
            mGetArticleContent = new AxisServiceStub.GetArticleContent();
            mAddArticle = new AxisServiceStub.AddArticle();
            mRemoveArticle = new AxisServiceStub.RemoveArticle();
            mSetArticleContent = new AxisServiceStub.SetArticleContent();
                        
        } catch (Exception ex) {
            log(ex.getMessage());
        }
    }

    @Override
    public String[] getArticlesId() {
        try {
            log("getArticles()");
            
            mGetArticlesIdResponse = mService.getArticlesId(mGetArticlesId);
            return mGetArticlesIdResponse.get_return();
        } catch (RemoteException ex) {
            log(ex.getMessage());
        }
        return null;
    }
    
    @Override
    public String getArticleName(String articleId) {
        log("getArticleContent(" + articleId + ")");
        
        mGetArticleName.setArticleId(articleId);
        try {
            mGetArticleNameResponse = mService.getArticleName(mGetArticleName);
        } catch (RemoteException ex) {
            log(ex.getMessage());
            return null;
        }
        return mGetArticleNameResponse.get_return();
    }

    @Override
    public String getArticleContent(String articleId) {
        log("getArticleContent(" + articleId + ")");
        
        mGetArticleContent.setArticleId(articleId);
        try {
            mGetArticleContentResponse = mService.getArticleContent(mGetArticleContent);
        } catch (RemoteException ex) {
            log(ex.getMessage());
            return null;
        }
        return mGetArticleContentResponse.get_return();
    }

    @Override
    public String addArticle(String articleName) {
        log("addArticle(" + articleName + ")");
        
        mAddArticle.setArticleName(articleName);
        try {
            mAddArticleResponse = mService.addArticle(mAddArticle);
        } catch (RemoteException ex) {
            log(ex.getMessage());
            return ex.getMessage();
        }
        return mAddArticleResponse.get_return();
    }

    @Override
    public String removeArticle(String articleId) {
        log("removeArticle(" + articleId + ")");
        
        mRemoveArticle.setArticleId(articleId);
        try {
            mRemoveArticleResponse = mService.removeArticle(mRemoveArticle);
        } catch (RemoteException ex) {
            log(ex.getMessage());
            return ex.getMessage();
        }
        return mRemoveArticleResponse.get_return();
    }

    @Override
    public String setArticleContent(String articleId, String articleContent) {
        log("setArticleContent(" + articleId + ", " + articleContent + ")");
        
        mSetArticleContent.setArticleId(articleId);
        mSetArticleContent.setArticleContent(articleContent);
        try {
            mSetArticleContentResponse = mService.setArticleContent(mSetArticleContent);
        } catch (RemoteException ex) {
            log(ex.getMessage());
            return ex.getMessage();
        }
        return mSetArticleContentResponse.get_return();
    }
    
    private static void log(String str) {
        //System.out.println("ClientDocOriented: " + str);
    }
    
}
