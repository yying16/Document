drop database if exists dataSource;
create database dataSource;
use dataSource;
drop table if exists t_user;
create table t_user(
    account varchar(30) not null comment '账号',
    password varchar(30) not null comment '密码',
    username varchar(30) not null comment '用户名',
    status int default 0 not null comment '身份', /* 1表示管理员，0表示普通用户*/
    telephone varchar(30) not null comment '手机号码',
    email varchar(50) not null comment '邮箱',
    primary key (account)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
create index index_account on t_user(account ASC);
insert into t_user values ('zhangsan','zs123456','张三',0,'15915712354','zhangsan@163.com'),
                          ('lisi','ls123456','李四',0,'13430241235','lisi@qq.com'),
                          ('wangwu','ww123456','王五',0,'13645236589','wangwu@163.com'),
                          ('chenliu','cl123456','陈六',0,'13316397963','chenliu@163.com'),
                          ('xuqi','xq123456','许七',0,'13352679568','xuqi@163.com'),
                          ('maba','mb123456','马八',0,'13654879632','maba@163.com'),
                          ('zhengjiu','zj123456','郑九',0,'13912546983','zhengjiu@163.com'),
                          ('huangshi','hs123456','黄十',0,'15815632498','huangshi@163.com'),
                          ('manager1','mg1123456','管理员01',1,'13546592356','manager01@163.com'),
                          ('manager2','mg2123456','管理员02',1,'13422975523','manager02@163.com'),
                          ('manager3','mg3123456','管理员03',1,'13018454961','manager03@163.com'),
                          ('manager4','mg4123456','管理员04',1,'15423615469','manager04@163.com');

drop table if exists t_book;
create table t_book(
    isbn varchar(30) not null comment 'ISBN书号',
    book_name varchar(30) not null comment '书名',
    author varchar(30) not null comment '作者',
    publisher varchar(30) not null comment '出版社',
    edition varchar(20) not null comment '版次',
    price float not null comment '价格',
    total int not null comment '藏书总数',
    remain int not null comment '馆内剩余',
    place varchar(30) not null comment '存放位置',
    deleted boolean default false,
    primary key (ISBN)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
create index index_ISBN on t_book(ISBN ASC );
insert into t_book(ISBN, book_name, author, publisher, edition, price, total, remain, place) values
                           ('9787536484276','球状闪电','刘慈欣','四川科学技术出版社','典藏版',25.00,30,10,'4-A-15-423'),
                           ('9787530221532','活着','余华','北京十月文艺出版社','第二版',35.00,40,20,'3-A-4-342'),
                           ('9787020002207','红楼梦','曹雪芹','人民文学出版社','第三版',50.8,30,25,'2-A-3-441'),
                           ('9787549119943','水浒传','施耐庵','南方日报出版社','第一版',30.00,40,20,'2-A-2-326'),
                           ('9787020008728','三国演义','罗贯中','人民文学出版社','第五版',20.00,25,10,'4-B-4-356'),
                           ('9787500867869','西游记','吴承恩','中国工人出版社','第四版',30.00,30,15,'2-B-4-348'),
                           ('9787534256301','狼王梦','沈石溪','浙江少年儿童出版社','第四版',29.00,10,5,'1-A-1-268'),
                           ('9787040396638','高等数学','同济大学数学教研室','高等教育出版社','第三版',50.00,20,0,'2-B-5-542'),
                           ('9787111589105','软件工程导论','张海藩','清华大学出版社','第六版',15.00,30,15,'1-B-4-242'),
                           ('9787513517119','学术英语','季佩英','外语教学与研究出版社','第五版',25.00,40,25,'5-A-1-162'),
                           ('9787563523276','线性代数','张忠挥','北京邮电大学出版社','第三版',30.00,20,5,'1-A-1-412'),
                          ('9787302511052','设计模式','刘伟','清华大学出版社','第二版',79.8,20,10,'3-B-3-254');


drop table if exists t_record;
create table t_record(
    record_id int auto_increment,
    account varchar(30) not null comment '账号',
    isbn varchar(30) not null comment '书号',
    borrow_time date not null comment '借阅时间',
    date_to_return date not null,
    return_time date default null comment '归还时间', /*默认为空*/
    state varchar(20) not null default '未归还' comment '归还状态', /*已归还 未归还 两种状态*/
    primary key (record_id),
    foreign key (account) references t_user(account),
    foreign key (ISBN) references t_book(ISBN)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
create index index_record on t_record(account,ISBN ASC);
insert into t_record(account, ISBN, borrow_time,date_to_return,return_time,state) values
                            ('zhangsan','9787536484276','2022-04-30','2022-05-30','2022-05-23','已归还'),
                            ('zhangsan','9787530221532','2022-04-30','2022-05-30','2022-05-23','已归还'),
                            ('lisi','9787020008728','2022-05-02','2022-06-02','2022-05-22','已归还'),
                            ('lisi','9787500867869','2022-05-02','2022-06-02','2022-05-22','已归还'),
                            ('lisi','9787020002207','2022-05-02','2022-06-02','2022-05-22','已归还'),
                            ('wangwu','9787534256301','2022-05-10','2022-06-10',null,'未归还'),
                            ('wangwu','9787549119943','2022-05-10','2022-06-10',null,'未归还'),
                            ('wangwu','9787513517119','2022-05-10','2022-06-10',null,'未归还'),
                            ('chenliu','9787563523276','2022-05-15','2022-06-15','2022-05-25','已归还'),
                            ('chenliu','9787040396638','2022-05-15','2022-06-15','2022-05-25','已归还'),
                            ('chenliu','9787302511052','2022-05-15','2022-06-15','2022-05-25','已归还'),
                            ('xuqi','9787111589105','2022-05-20','2022-06-20','2022-06-10','已归还'),
                            ('xuqi','9787513517119','2022-05-20','2022-06-20','2022-06-10','已归还');
create view v_record
as
    select r.account ,b.ISBN,record_id,book_name,author ,publisher ,edition ,borrow_time ,date_to_return,return_time ,state
    from t_record r,t_book b,t_user u
    where r.ISBN = b.ISBN and r.account = u.account


