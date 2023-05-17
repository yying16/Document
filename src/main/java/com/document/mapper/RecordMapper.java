package com.document.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.document.domain.Record;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    @Insert("insert into t_record(account, isbn, borrow_time,date_to_return,return_time,state) values(#{account},#{isbn},#{borrowTime},#{dateToReturn},#{returnTime},#{state})")
    public void insertRecord(Record record);

    //根据账号和ISBN删除借阅记录 √
    @Delete("delete from v_record where account=#{account} and ISBN=#{isbn}")
    public void deleteRecord(Record record);

    //根据账号更新借阅记录 √
    @Update("update t_record set account=#{account},ISBN=#{isbn},borrow_time=#{borrowTime},date_to_return=#{dateToReturn},return_time=#{returnTime},state=#{state} where record_id=#{recordId}")
    public void updateRecord(Record record);

    @Select("select* from v_record where record_id = (select max(record_id)from v_record where account=#{account} and ISBN=#{isbn})")
    public Record getRecentRecordByISBNAndAccount(Record record);

    @Select("select* from v_record where account=#{account} and ISBN=#{isbn}")
    public Record[] getRecordByISBNAndAccount(Record record);
}
