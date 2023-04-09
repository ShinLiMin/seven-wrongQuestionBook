create database wrongQuestionBook;
CREATE TABLE t_user (
                        id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                        phone varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
                        email varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
                        password varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
                        salt varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '盐值',
                        createTime datetime DEFAULT NULL COMMENT '创建时间',
                        updateTime datetime DEFAULT NULL COMMENT '更新时间',
                        PRIMARY KEY (id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
CREATE TABLE t_user_info (
                             id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                             userId bigint DEFAULT NULL COMMENT '用户id',
                             nick varchar(100) DEFAULT NULL COMMENT '昵称',
                             avatar varchar(255) DEFAULT NULL COMMENT '头像',
                             sign text COMMENT '签名',
                             gender varchar(2) DEFAULT NULL COMMENT '性别 ：0男 1女 2未知',
                             birth varchar(20) DEFAULT NULL COMMENT '生日',
                             createTime datetime DEFAULT NULL COMMENT '创建时间',
                             updateTime datetime DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户基本信息表';

CREATE TABLE t_refresh_token (
                                 id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                 userId bigint DEFAULT NULL COMMENT '用户id',
                                 refreshToken varchar(500) DEFAULT NULL COMMENT '刷新令牌',
                                 createTime datetime DEFAULT NULL COMMENT '创建时间',
                                 PRIMARY KEY (id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='刷新令牌记录表';
CREATE TABLE t_file (
                        id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                        url varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件存储路径',
                        type varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件类型',
                        md5 varchar(500) DEFAULT NULL COMMENT '文件md5唯一标识',
                        createTime datetime DEFAULT NULL COMMENT '创建时间',
                        PRIMARY KEY (id)
)
    ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件信息表';

CREATE TABLE t_note_comment (
                                 id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                 userId bigint NOT NULL COMMENT '用户id',
                                 noteId bigint NOT NULL COMMENT '笔记id',
                                 comment text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评论内容',
                                 replyUserId bigint DEFAULT NULL COMMENT '回复用户id',
                                 rootId bigint DEFAULT NULL COMMENT '根节点评论id',
                                 createTime datetime DEFAULT NULL COMMENT '创建时间',
                                 updateTime datetime DEFAULT NULL COMMENT '更新时间',
                                 PRIMARY KEY (id) USING BTREE
)
    ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT = dynamic COMMENT='笔记评论表';

CREATE TABLE t_note_comment_like (
                                      id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                      userId bigint DEFAULT NULL COMMENT '用户id',
                                      noteId bigint NOT NULL COMMENT '笔记id',
                                      commentId bigint NOT NULL COMMENT '评论id',
                                      createTime datetime DEFAULT NULL COMMENT '创建时间',
                                      PRIMARY KEY (id) USING BTREE
)
    ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论点赞表';
create table t_sensitive_word(
                                   id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                   value varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '敏感词',
                                   type int(10) default 0,
                                   createTime datetime DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='敏感词表';

CREATE TABLE t_note(
                        id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                        userId bigint DEFAULT NULL COMMENT '用户id',
                        thumbnail varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '封面链接',
                        title varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '笔记标题',
                        content LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '笔记内容',
                        type varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '笔记类型 0原创 1转载',
                        area varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分区',
                        createTime datetime DEFAULT NULL COMMENT '创建时间',
                        updateTime datetime DEFAULT NULL COMMENT '更新时间',
                        PRIMARY KEY (id)
)
    ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='笔记表';

CREATE TABLE t_note_like (
                                     id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                     userId bigint NOT NULL COMMENT '用户id',
                                     noteId bigint NOT NULL COMMENT '笔记id',
                                     createTime datetime DEFAULT NULL COMMENT '创建时间',
                                     PRIMARY KEY (id) USING BTREE
)
    ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='笔记点赞表';

CREATE TABLE t_note_tag (
                             id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                             videoId bigint NOT NULL COMMENT '笔记id',
                             tagId bigint NOT NULL COMMENT '标签id',
                             createTime datetime DEFAULT NULL COMMENT '创建时间',
                             PRIMARY KEY (id)
)
    ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='笔记相关标签表';

CREATE TABLE t_tag (
                       id bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                       name varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
                       createTime datetime DEFAULT NULL COMMENT '创建时间',
                       PRIMARY KEY (id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签表';