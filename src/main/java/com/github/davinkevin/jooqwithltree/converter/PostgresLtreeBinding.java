package com.github.davinkevin.jooqwithltree.converter;

import org.jooq.*;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Objects;

import static java.util.Objects.isNull;

public class PostgresLtreeBinding implements Binding<Object, String> {
    @Override
    public Converter<Object, String> converter() {
        return new Converter<Object, String>() {
            @Override
            public String from(Object dbObject) {
                return isNull(dbObject) ? null : dbObject.toString();

            }

            @Override
            public Object to(String userObject) {
                return userObject;
            }

            @Override
            public Class<Object> fromType() {
                return Object.class;
            }

            @Override
            public Class<String> toType() {
                return String.class;
            }
        };

    }

    @Override
    public void sql(BindingSQLContext<String> ctx) throws SQLException {
        if (ctx.render().paramType() == ParamType.INLINED)
            ctx.render().visit(DSL.inline(ctx.convert(converter()).value()));
        else
            ctx.render().sql("?");
    }

    @Override
    public void register(BindingRegisterContext<String> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(BindingSetStatementContext<String> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    @Override
    public void set(BindingSetSQLOutputContext<String> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(BindingGetResultSetContext<String> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<String> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<String> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
