package com.datawarehouse;

import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DatawarehouseConfig.class)
@AutoConfigureDataJdbc
public class BaseTest {
}