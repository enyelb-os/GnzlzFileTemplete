package tools.gnzlz.templete;

import tools.gnzlz.templete.Template.Template;

import java.util.Hashtable;

public class main {

    public static void main(String[] args) {

        Hashtable<String, Object> data = new Hashtable<String, Object>();

        data.put("dateTypes","DataTypes");
        data.put("sequelize","Sequelize");



        Decoder.decoder(data,
            """
             import { @VAR{dateTypes} } from '@VAR{sequelize.toLowerCase()}';")
            @FOR{ACRelation.class, table.hasOneNameImports(), (content, relation, index) -> {
                content.Line("import " , relation.pkColumn.table.nameCamelCase() , " from '../../model/" , relation.pkColumn.table.name , ".js';");
            })
            .Line("import " , table.nameCamelCase() , "Validation from '../../validation/model/" , table.name , ".js';").Line(1)
            .Line("import ",  catalog.nameCamelCase() , "Database from '../../../database.js';").Line(1)
            .Template(Block.New("const ", table.nameCamelCase(), " = ").Block(Content.New()
                    .Template(Block.New("attributes : ").Block(Content.New()
                            .ForFunction(ACColumn.class, table.columns, (content, column, index) -> {
                                if(index != 0) content.Text(",");
                                content.Template(Block.New(column.name, ": ").Block(Content.New()
                                        .Line("type: ", dateTypes, ".", typeValue(column.type), ",")
                                        .Line("allowNull: ", column.nullable ? "true" : "false", ",")
                                        .Line("validate: ", table.nameCamelCase(), "Validation.", column.name, ",")
                                        .Line(!column.def.isEmpty(), "defaultValue: ", typeDefault(column.type, column.def), ",")
                                        .Line(column.autoincrement, "autoIncrement: true,")
                                        .Line(column.isPrimaryKey(), "primaryKey: true,")
                                        .ForFunction(ACRelation.class, column.hasOne(), (contentRelation, relation, indexRelation)->{
                                            contentRelation.Template(Block.New("references: ").Block(Content.New()
                                                    .Line("model: ", relation.pkColumn.table.nameCamelCase(), ",")
                                                    .Line("key: '", relation.pkColumn.name ,"'")
                                            ));
                                        })
                                ));
                            })
                    )).Text(",")
                    .Template(Block.New("options : ").Block(Content.New()
                            .Line(sequelize.toLowerCase(), ": ", catalog.nameCamelCase() , "Database,")
                            .Line("modelName: '" , table.nameCamelCase() , "',")
                            .Line("tableName: '" , table.name , "',")
                            .Line("timestamps: false,")
                            .Line("createdAt: false,")
                            .Line("updatedAt: false")
                    ))
            )).Line(1)
            .Line("/*******************************************")
            .Line(" * Export ")
            .Line(" *******************************************/").Line(1)
            .Line("export default ", table.nameCamelCase(), ";")
            """
        ,"@VAR{", "}");

        new Template("""
            import { @VAR{dateTypes} } from '@VAR{sequelize.toLowerCase()}'
            @FOR{ table.hasOneNameImports() | relation | index}
                import @VAR{relation.pkColumn.table.nameCamelCase()} from '../../model/@VAR{relation.pkColumn.table.name}.js';
            @ENDFOR
            import @VAR{table.nameCamelCase()}Validation from '../../validation/model/@VAR{table.name}.js'
            
            import @VAR{catalog.nameCamelCase()}Database from '../../../database.js';
            
            const @VAR{table.nameCamelCase()} = {
                attributes : {
                    @FOR{ table.columns | column | index}
                        @IF{index != 0} , @ENDIF;
                        @VAR{column.name} : {
                            type: @VAR{dateTypes}.@VAR{typeValue(column.type)},
                            allowNull: @IF{column.nullable} true @ELSE false @ENDIF,
                            validate: @VAR{table.nameCamelCase()}Validation.@VAR{column.name},
                            @IF{!column.def.isEmpty()} defaultValue: @VAR{typeDefault(@VAR{column.type}, @VAR{column.def})}, @ENDIF
                            @IF{column.autoincrement} autoIncrement: true @ENDIF
                            @IF{column.isPrimaryKey()} primaryKey: true @ENDIF
                            @FOR{column.hasOne() | relation | indexRelation}
                                references: {
                                   model: @VAR{relation.pkColumn.table.nameCamelCase()},
                                   key: '@VAR{relation.pkColumn.name}'
                                }
                           @ENDFOR
                       },
                    @ENDFOR
               },
               options : {
                    @VAR{sequelize.toLowerCase()} : @VAR{catalog.nameCamelCase()}Database
                    modelName: '@VAR{table.nameCamelCase()}',
                    tableName: '@VAR{table.name}',
                    timestamps: false,
                    createdAt: false,
                    updatedAt: false
                }
            }
            
            /*******************************************
             * Export 
             *******************************************/
            
            export default @VAR{table.nameCamelCase()};
            ""","@","{", "}").build();
    }
}
