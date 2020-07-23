package special;

import util.DBUtils;
import util.CacheMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MessageCacheAccess {

    public static CacheMessage getDeletedMessage(Long messageId) {
        try {
            PreparedStatement statement = DBUtils.getMessageWithMessageId();
            statement.setLong(1, messageId);

            ResultSet resultSet = statement.executeQuery();

            CacheMessage cacheMessage = new CacheMessage();

            while (resultSet.next()) {
                cacheMessage.setMessageId(resultSet.getLong("MessageId"));
                cacheMessage.setMessageContent(resultSet.getString("MessageContent"));
                cacheMessage.setMessageSender(resultSet.getString("MessageSender"));
                cacheMessage.setDateSent(resultSet.getDate("DateSent").toLocalDate());
            }

            return cacheMessage;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int insertMessage(Long messageId, String messageContent, String messageSender, String dateSent) {
        try {
            PreparedStatement statement = DBUtils.getPreparedMessageLogStatement();
            statement.setLong(1, messageId);
            statement.setString(2, messageContent);
            statement.setString(3, messageSender);
            statement.setTimestamp(4, Timestamp.valueOf(dateSent));

            return statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
