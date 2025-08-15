package inhouse.digital.trainsystem.base.utils;

public sealed interface LoadSqlResult permits
        LoadSqlFromFileSuccess,
        LoadSqlFromFileError
{ }
