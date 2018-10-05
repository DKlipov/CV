package ru.nsuem.view;

import com.google.gson.JsonObject;
import ru.nsuem.sql.SQLHelper;

import java.sql.SQLException;

public interface IJsonView {
    String getName();
    JsonObject asJson() throws SQLException;
}
