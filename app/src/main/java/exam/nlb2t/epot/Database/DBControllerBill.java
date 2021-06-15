package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.core.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

public class DBControllerBill extends  DatabaseController{
    public boolean addBill(int userID, String address, List<Pair<Integer, Integer>> list)
    {
        boolean rs = false;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("DECLARE @para as [dbo].[BILL_INFOList];");

            for(Pair<Integer, Integer> p: list)
            {
                builder.append("INSERT INTO @para VALUES(?,?);");
            }
            builder.append("EXEC dbo.createBill ?, ?,@para;");

            PreparedStatement statement = connection.prepareStatement(builder.toString());

            int i = 1;
            for(Pair<Integer, Integer> p: list)
            {
                statement.setInt(i, p.first);
                i++;
                statement.setInt(i, p.second);
                i++;
            }
            statement.setInt(i, userID);
            i++;
            statement.setString(i, address);
            statement.addBatch();

            int[] r = statement.executeBatch();

            Log.d("MY_TAG", Arrays.toString(r));
            rs = true;
            commit();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            rollback();
            ErrorMsg = e.getMessage();
        }
        return rs;
    }
}
