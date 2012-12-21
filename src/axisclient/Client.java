/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axisclient;

import java.util.TreeMap;

/** Интерфейс для получения данных справочника
 *
 * @author Oleg Beloglazov
 */
public interface Client {
    
    /** Получить идентификаторы статей
     * 
     * @return идентификаторы всех статей, или null если произошла ошибка
     */
    String[] getArticlesId();
    
    /** Получить название статьи
     * 
     * @param articleId идентификатор статьи
     * @return название статьи, или null, если произошла ошибка
     */
    String getArticleName(String articleId);

    /** Получить содержимое статьи
     * 
     * @param articleId идентификатор статьи
     * @return содержимое статьи, или null, если произошла ошибка
     */
    String getArticleContent(String articleId);
    
    /** Добавить статью
     * 
     * @param articleName название статьи
     * @return null если не было ошибок, иначе описание ошибки
     */
    String addArticle(String articleName);

    /** Удалить статью
     * 
     * @param articleName идентификатор статьи
     * @return null если не было ошибок, иначе описание ошибки
     */
    String removeArticle(String articleId);

    /** Установить содержимое статьи
     * 
     * @param articleId идентификатор статьи
     * @param articleContent содержимое статьи
     * @return null если не было ошибок, иначе описание ошибки
     */
    String setArticleContent(String articleId, String articleContent);
}
