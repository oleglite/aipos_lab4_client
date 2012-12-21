/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axisclient;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractListModel;

/** Модель данных справочника
 *
 * @author Oleg Beloglazov
 */
public class DataModel extends AbstractListModel {
    
    Client mClient;
    ArrayList<String> mArticles = new ArrayList<String>();
    String[] mArticlesId;
    String mFilter = "";
    boolean mIsServiceAvaliable = false;
    
    /** Получить количество статей
     * 
     * @return число статей
     */
    @Override
    public int getSize() {
        if(mArticles == null) {
            return 0;
        }
        int filteredSize = 0;
        for(int i = 0; i < mArticles.size(); i++) {
            if(mArticles.get(i).startsWith(mFilter)) {
                filteredSize++;
            }
        }
        return filteredSize;
    }

    /** Получить название статьи с заданным номером
     * 
     * @param index номер по порядку
     * @return название статьи
     */
    @Override
    public Object getElementAt(int index) {
        for(int i = 0; i <= index; i++) {
            if(!mArticles.get(i).startsWith(mFilter)) {
                index++;
            }
        }
        return mArticles.get(index);
    }
    
    /** Получить содержимое статьи
     * 
     * @param articleNumber номер статьи
     * @return содержимое статьи, или null, если произошла ошибка
     */
    public String getArticleContent(int articleNumber) {        
        return mClient.getArticleContent(mArticlesId[articleNumber]);
    }
    
    /** Добавить статью
     * 
     * @param articleName название статьи
     */
    public void addArticle(String articleName) throws Exception {
        String responce = mClient.addArticle(articleName);
        update();
        if(responce != null) {
            throw new Exception(responce);
        }
    }
    
    /** Удалить статью
     * 
     * @param articleNumber номер статьи
     */
    public void removeArticle(int articleNumber) throws Exception {
        String responce = mClient.removeArticle(mArticlesId[articleNumber]);
        update();
        if(responce != null) {
            throw new Exception(responce);
        }
    }
    
    /** Установить содержимое статьи
     * 
     * @param articleNumber номер статьи
     * @param articleContent содержимое статьи
     */
    public void setArticleContent(int articleNumber, String articleContent) throws Exception {
        String responce = mClient.setArticleContent(mArticlesId[articleNumber], articleContent);
        if(responce != null) {
            throw new Exception(responce);
        }
    }
    
    /** Установить фильтр
     * Фильтр - набор символов с которых начинается искомое название.
     * @param filter фильтрующее слово
     */
    public void setFilter(String filter) {
        mFilter = filter;
        update();
    }
    
    /** Установить клиент
     *  Клиент должен быть доступен.
     * @param client клиент
     */
    public void setClient(Client client) {
        mClient = client;
        if(mClient != null) {
            update();
        }
    }
    
    /** Проверить доступен ли используемый сервис
     * 
     * @return true, если сервис доступен
     */
    public boolean isServiceAvaliable() {
        return mIsServiceAvaliable;
    }
    
    /** Метод, вызываемый после обновления данных
     * 
     */
    private void update() {
        int articlesNumber = 0;
        if(mClient != null && mArticles != null) {
            articlesNumber = mArticles.size();
        }        
        loadArticles();
        if(mArticles.size() > articlesNumber) {
            articlesNumber = mArticles.size();
        }
        fireContentsChanged(this, 0, articlesNumber);
    }
    
    private void loadArticles() {
        mArticlesId = mClient.getArticlesId();
        if(mArticlesId != null) {
            mArticles.clear();
            for(String id : mArticlesId) {
                mArticles.add(mClient.getArticleName(id));
            }
            mIsServiceAvaliable = true;
        } else {
            mArticles = new ArrayList<String>();
            mIsServiceAvaliable = false;
        }
    }
}
