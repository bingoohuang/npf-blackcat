package com.github.bingoohuang.blackcat.utils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class XmlMinifierTest {
    @Test
    public void minifySimple() {
        assertThat(XmlMinifier.minify("<abc><efg>value</efg></abc>"))
                .isEqualTo("abc:efg:value$$");
        assertThat(XmlMinifier.recover("abc:efg:value$$"))
                .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><abc><efg>value</efg></abc>");
    }

    public static final String MINIFIED_XML = "UniBSS:OrigDomain:UESS$HomeDomain:UCRM$BIPCode:BIP2F007$BIPVer:0100$ActivityCode:T2030509$ActionCode:1$ActionRelation:0$Routing:RouteType:00$RouteValue:51$$ProcID:PROC2016020311075604911703911$TransIDO:SSP2016020311075604911703909$TransIDH:2016020311075769041418$ProcessTime:20160203110823$Response:RspType:0$RspCode:0000$RspDesc:success$$SPReserve:TransIDC:201602031107570029804701708101$CutOffDay:20160203$OSNDUNS:0400$HSNDUNS:5100$ConvID:SSP201602031107560491170390920160203110757241$$TestFlag:0$MsgSender:0002$MsgReceiver:0002$SvcContVer:0100$SvcCont:UserCheckRsp:RespCode:0000$RespDesc:成功$CustMess:Custid:1234567890123456$CustName:狐狸精$CertType:02$CertNum:123456789012345678$CertAddr:儿女国五子县盘丝镇盘丝洞一号洞39号$CustLvl:无等级$CustomerType:01$CustomerLoc:0762$CustomerAddr:儿女国五子县盘丝镇盘丝洞一号洞39号$DevChnlID:51aa496$$UserMess:NetType:01$SubscrbID:6211234567850998$SubscrbStat:10$Brand:6$ProvCode:51$CityCode:670$OpenDate:20150113115555$ActiveDate:20150113115555$LandLvl:4$RoamStat:3$ProductID:16169659$ProductName:(OCS)双4G河源卡升级版套餐$NextProductID:16169659$NextProductName:(OCS)双4G河源卡升级版套餐$Simcard:8981234567890123456$CreditVale:0$SubscrbType:1$InteNetType:1$ArrearageFee:0$$AcctMess:AcctID:6211112223334455$AcctType:0$AcctName:狐狸精$SendFlag:2$PayType:10$SendContent:05$SendEmail:$$Para:ParaID:0$ParaValue:0$$$$$";

    @Test
    public void minifyComplex() {
        String src = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<UniBSS><OrigDomain>UESS</OrigDomain><HomeDomain>UCRM</HomeDomain><BIPCode>BIP2F007</BIPCode><BIPVer>0100</BIPVer><ActivityCode>T2030509</ActivityCode><ActionCode>1</ActionCode><ActionRelation>0</ActionRelation><Routing><RouteType>00</RouteType><RouteValue>51</RouteValue></Routing><ProcID>PROC2016020311075604911703911</ProcID><TransIDO>SSP2016020311075604911703909</TransIDO><TransIDH>2016020311075769041418</TransIDH><ProcessTime>20160203110823</ProcessTime><Response><RspType>0</RspType><RspCode>0000</RspCode><RspDesc>success</RspDesc></Response><SPReserve><TransIDC>201602031107570029804701708101</TransIDC><CutOffDay>20160203</CutOffDay><OSNDUNS>0400</OSNDUNS><HSNDUNS>5100</HSNDUNS><ConvID>SSP201602031107560491170390920160203110757241</ConvID></SPReserve><TestFlag>0</TestFlag><MsgSender>0002</MsgSender><MsgReceiver>0002</MsgReceiver><SvcContVer>0100</SvcContVer><SvcCont><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<UserCheckRsp>\n" +
                "    <RespCode>0000</RespCode>\n" +
                "    <RespDesc>成功</RespDesc>\n" +
                "    <CustMess>\n" +
                "        <Custid>1234567890123456</Custid>\n" +
                "        <CustName>狐狸精</CustName>\n" +
                "        <CertType>02</CertType>\n" +
                "        <CertNum>123456789012345678</CertNum>\n" +
                "        <CertAddr>儿女国五子县盘丝镇盘丝洞一号洞39号</CertAddr>\n" +
                "        <CustLvl>无等级</CustLvl>\n" +
                "        <CustomerType>01</CustomerType>\n" +
                "        <CustomerLoc>0762</CustomerLoc>\n" +
                "        <CustomerAddr>儿女国五子县盘丝镇盘丝洞一号洞39号</CustomerAddr>\n" +
                "        <DevChnlID>51aa496</DevChnlID>\n" +
                "    </CustMess>\n" +
                "    <UserMess>\n" +
                "        <NetType>01</NetType>\n" +
                "        <SubscrbID>6211234567850998</SubscrbID>\n" +
                "        <SubscrbStat>10</SubscrbStat>\n" +
                "        <Brand>6</Brand>\n" +
                "        <ProvCode>51</ProvCode>\n" +
                "        <CityCode>670</CityCode>\n" +
                "        <OpenDate>20150113115555</OpenDate>\n" +
                "        <ActiveDate>20150113115555</ActiveDate>\n" +
                "        <LandLvl>4</LandLvl>\n" +
                "        <RoamStat>3</RoamStat>\n" +
                "        <ProductID>16169659</ProductID>\n" +
                "        <ProductName>(OCS)双4G河源卡升级版套餐</ProductName>\n" +
                "        <NextProductID>16169659</NextProductID>\n" +
                "        <NextProductName>(OCS)双4G河源卡升级版套餐</NextProductName>\n" +
                "        <Simcard>8981234567890123456</Simcard>\n" +
                "        <CreditVale>0</CreditVale>\n" +
                "        <SubscrbType>1</SubscrbType>\n" +
                "        <InteNetType>1</InteNetType>\n" +
                "        <ArrearageFee>0</ArrearageFee>\n" +
                "    </UserMess>\n" +
                "    <AcctMess>\n" +
                "        <AcctID>6211112223334455</AcctID>\n" +
                "        <AcctType>0</AcctType>\n" +
                "        <AcctName>狐狸精</AcctName>\n" +
                "        <SendFlag>2</SendFlag>\n" +
                "        <PayType>10</PayType>\n" +
                "        <SendContent>05</SendContent>\n" +
                "        <SendEmail></SendEmail>\n" +
                "    </AcctMess>\n" +
                "    <Para>\n" +
                "        <ParaID>0</ParaID>\n" +
                "        <ParaValue>0</ParaValue>\n" +
                "    </Para>\n" +
                "</UserCheckRsp>\n" +
                "]]></SvcCont></UniBSS>";

        String result = XmlMinifier.minify(src);

        assertThat(result).isEqualTo(MINIFIED_XML);
    }


    @Test
    public void recover() {
        String recover = XmlMinifier.recover(MINIFIED_XML);
        assertThat(recover).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><UniBSS><OrigDomain>UESS</OrigDomain><HomeDomain>UCRM</HomeDomain><BIPCode>BIP2F007</BIPCode><BIPVer>0100</BIPVer><ActivityCode>T2030509</ActivityCode><ActionCode>1</ActionCode><ActionRelation>0</ActionRelation><Routing><RouteType>00</RouteType><RouteValue>51</RouteValue></Routing><ProcID>PROC2016020311075604911703911</ProcID><TransIDO>SSP2016020311075604911703909</TransIDO><TransIDH>2016020311075769041418</TransIDH><ProcessTime>20160203110823</ProcessTime><Response><RspType>0</RspType><RspCode>0000</RspCode><RspDesc>success</RspDesc></Response><SPReserve><TransIDC>201602031107570029804701708101</TransIDC><CutOffDay>20160203</CutOffDay><OSNDUNS>0400</OSNDUNS><HSNDUNS>5100</HSNDUNS><ConvID>SSP201602031107560491170390920160203110757241</ConvID></SPReserve><TestFlag>0</TestFlag><MsgSender>0002</MsgSender><MsgReceiver>0002</MsgReceiver><SvcContVer>0100</SvcContVer><SvcCont><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><UserCheckRsp><RespCode>0000</RespCode><RespDesc>成功</RespDesc><CustMess><Custid>1234567890123456</Custid><CustName>狐狸精</CustName><CertType>02</CertType><CertNum>123456789012345678</CertNum><CertAddr>儿女国五子县盘丝镇盘丝洞一号洞39号</CertAddr><CustLvl>无等级</CustLvl><CustomerType>01</CustomerType><CustomerLoc>0762</CustomerLoc><CustomerAddr>儿女国五子县盘丝镇盘丝洞一号洞39号</CustomerAddr><DevChnlID>51aa496</DevChnlID></CustMess><UserMess><NetType>01</NetType><SubscrbID>6211234567850998</SubscrbID><SubscrbStat>10</SubscrbStat><Brand>6</Brand><ProvCode>51</ProvCode><CityCode>670</CityCode><OpenDate>20150113115555</OpenDate><ActiveDate>20150113115555</ActiveDate><LandLvl>4</LandLvl><RoamStat>3</RoamStat><ProductID>16169659</ProductID><ProductName>(OCS)双4G河源卡升级版套餐</ProductName><NextProductID>16169659</NextProductID><NextProductName>(OCS)双4G河源卡升级版套餐</NextProductName><Simcard>8981234567890123456</Simcard><CreditVale>0</CreditVale><SubscrbType>1</SubscrbType><InteNetType>1</InteNetType><ArrearageFee>0</ArrearageFee></UserMess><AcctMess><AcctID>6211112223334455</AcctID><AcctType>0</AcctType><AcctName>狐狸精</AcctName><SendFlag>2</SendFlag><PayType>10</PayType><SendContent>05</SendContent><SendEmail></SendEmail></AcctMess><Para><ParaID>0</ParaID><ParaValue>0</ParaValue></Para></UserCheckRsp>]]></SvcCont></UniBSS>");
    }
}
