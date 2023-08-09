package cn.note.redis;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class demo implements Serializable {

    private Integer name;


    private LocalDateTime time;
}
