-- MySQL dump 10.13  Distrib 5.6.14, for Win32 (x86)
--
-- Host: localhost    Database: intelliinvest
-- ------------------------------------------------------
-- Server version	5.6.14

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bhav_data`
--

DROP TABLE IF EXISTS `bhav_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bhav_data` (
  `EXCHANGE` varchar(8) DEFAULT NULL,
  `SYMBOL` varchar(20) DEFAULT NULL,
  `SERIES` varchar(4) DEFAULT NULL,
  `OPEN` double DEFAULT NULL,
  `HIGH` double DEFAULT NULL,
  `LOW` double DEFAULT NULL,
  `CLOSE` double DEFAULT NULL,
  `LAST` double DEFAULT NULL,
  `PREVCLOSE` double DEFAULT NULL,
  `TOTTRDQTY` bigint(20) DEFAULT NULL,
  `TOTTRDVAL` double DEFAULT NULL,
  `TIMESTAMP` date DEFAULT NULL,
  `TOTALTRADES` bigint(20) DEFAULT NULL,
  `ISIN` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bom_stocks`
--

DROP TABLE IF EXISTS `bom_stocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bom_stocks` (
  `CODE` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chat_data`
--

DROP TABLE IF EXISTS `chat_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_data` (
  `MESSAGE_ID` int(10) DEFAULT NULL,
  `USERNAME` varchar(25) DEFAULT NULL,
  `CHAT_TIME` datetime DEFAULT NULL,
  `CHAT_MESSAGE` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `COMMENT_ID` int(11) NOT NULL,
  `PARENT_ID` int(11) NOT NULL,
  `POST_ID` int(11) NOT NULL,
  `COMMENTER` varchar(255) DEFAULT NULL,
  `CONTENT` text,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`COMMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `current_stock_price`
--

DROP TABLE IF EXISTS `current_stock_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `current_stock_price` (
  `CODE` varchar(100) DEFAULT NULL,
  `CURRENT_PRICE` double DEFAULT NULL,
  `CP` double DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eod_stock_price`
--

DROP TABLE IF EXISTS `eod_stock_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eod_stock_price` (
  `CODE` varchar(100) DEFAULT NULL,
  `EOD_DATE` date DEFAULT NULL,
  `EOD_PRICE` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `intelli_invest_data`
--

DROP TABLE IF EXISTS `intelli_invest_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intelli_invest_data` (
  `CODE` varchar(100) DEFAULT NULL,
  `EOD_PRICE` double DEFAULT NULL,
  `SIGNAL_PRICE` double DEFAULT NULL,
  `YESTERDAY_SIGNAL_TYPE` varchar(15) DEFAULT NULL,
  `SIGNAL_TYPE` varchar(15) DEFAULT NULL,
  `QUARTERLY` double DEFAULT NULL,
  `HALF_YEARLY` double DEFAULT NULL,
  `NINE_MONTHS` double DEFAULT NULL,
  `YEARLY` double DEFAULT NULL,
  `RISK_RETURN_GROUP` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `key_store`
--

DROP TABLE IF EXISTS `key_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `key_store` (
  `type` varchar(100) DEFAULT NULL,
  `value` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `magic_numbers`
--

DROP TABLE IF EXISTS `magic_numbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `magic_numbers` (
  `SYMBOL` varchar(100) DEFAULT NULL,
  `MAGIC_NUMBER` int(10) DEFAULT NULL,
  `PNL` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `manage_portfolio_details`
--

DROP TABLE IF EXISTS `manage_portfolio_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manage_portfolio_details` (
  `ID` varchar(50) DEFAULT NULL,
  `USER_ID` varchar(25) DEFAULT NULL,
  `CODE` varchar(10) DEFAULT NULL,
  `TRADE_DATE` date DEFAULT NULL,
  `DIRECTION` varchar(10) DEFAULT NULL,
  `QUANTITY` double DEFAULT NULL,
  `PRICE` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nifty_stocks`
--

DROP TABLE IF EXISTS `nifty_stocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nifty_stocks` (
  `CODE` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nse_to_bse_map`
--

DROP TABLE IF EXISTS `nse_to_bse_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nse_to_bse_map` (
  `NSE_CODE` varchar(100) DEFAULT NULL,
  `BSE_CODE` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `option_suggestions_data`
--

DROP TABLE IF EXISTS `option_suggestions_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `option_suggestions_data` (
  `CODE` varchar(100) DEFAULT NULL,
  `INSTRUMENT` varchar(10) DEFAULT NULL,
  `EXPIRY_DATE` date DEFAULT NULL,
  `STRIKE_PRICE` double DEFAULT NULL,
  `OPTION_TYPE` varchar(10) DEFAULT NULL,
  `OPTION_PRICE` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_details`
--

DROP TABLE IF EXISTS `payment_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_details` (
  `NO_OF_STOCKS` int(10) DEFAULT NULL,
  `NO_OF_MONTHS` varchar(10) DEFAULT NULL,
  `AMOUNT` double DEFAULT NULL,
  `LINK` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
  `POST_ID` int(11) NOT NULL,
  `AUTHOR` varchar(255) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  `CONTENT` text,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`POST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `risk_return_matrix`
--

DROP TABLE IF EXISTS `risk_return_matrix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `risk_return_matrix` (
  `CODE` varchar(100) DEFAULT NULL,
  `TYPE_3M` varchar(8) DEFAULT NULL,
  `TYPE_6M` varchar(8) DEFAULT NULL,
  `TYPE_9M` varchar(8) DEFAULT NULL,
  `TYPE_12M` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signal_components_10`
--

DROP TABLE IF EXISTS `signal_components_10`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signal_components_10` (
  `SYMBOL` varchar(100) DEFAULT NULL,
  `SIGNAL_DATE` date DEFAULT NULL,
  `TR` double DEFAULT NULL,
  `PLUS_DM_1` double DEFAULT NULL,
  `MINUS_DM_1` double DEFAULT NULL,
  `TR_N` double DEFAULT NULL,
  `PLUS_DM_N` double DEFAULT NULL,
  `MINUS_DM_N` double DEFAULT NULL,
  `DIFF_DI_N` double DEFAULT NULL,
  `SUM_DI_N` double DEFAULT NULL,
  `DX` double DEFAULT NULL,
  `ADX_N` double DEFAULT NULL,
  `SPLIT_MULTIPLIER` double DEFAULT NULL,
  `PREVIOUS_SIGNAL_TYPE` varchar(15) DEFAULT NULL,
  `SIGNAL_TYPE` varchar(15) DEFAULT NULL,
  `SIGNAL_PRESENT` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signal_components_15`
--

DROP TABLE IF EXISTS `signal_components_15`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signal_components_15` (
  `SYMBOL` varchar(100) DEFAULT NULL,
  `SIGNAL_DATE` date DEFAULT NULL,
  `TR` double DEFAULT NULL,
  `PLUS_DM_1` double DEFAULT NULL,
  `MINUS_DM_1` double DEFAULT NULL,
  `TR_N` double DEFAULT NULL,
  `PLUS_DM_N` double DEFAULT NULL,
  `MINUS_DM_N` double DEFAULT NULL,
  `DIFF_DI_N` double DEFAULT NULL,
  `SUM_DI_N` double DEFAULT NULL,
  `DX` double DEFAULT NULL,
  `ADX_N` double DEFAULT NULL,
  `SPLIT_MULTIPLIER` double DEFAULT NULL,
  `SIGNAL_TYPE` varchar(15) DEFAULT NULL,
  `SIGNAL_PRESENT` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signal_components_5`
--

DROP TABLE IF EXISTS `signal_components_5`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signal_components_5` (
  `SYMBOL` varchar(100) DEFAULT NULL,
  `SIGNAL_DATE` date DEFAULT NULL,
  `TR` double DEFAULT NULL,
  `PLUS_DM_1` double DEFAULT NULL,
  `MINUS_DM_1` double DEFAULT NULL,
  `TR_N` double DEFAULT NULL,
  `PLUS_DM_N` double DEFAULT NULL,
  `MINUS_DM_N` double DEFAULT NULL,
  `DIFF_DI_N` double DEFAULT NULL,
  `SUM_DI_N` double DEFAULT NULL,
  `DX` double DEFAULT NULL,
  `ADX_N` double DEFAULT NULL,
  `SPLIT_MULTIPLIER` double DEFAULT NULL,
  `SIGNAL_TYPE` varchar(15) DEFAULT NULL,
  `SIGNAL_PRESENT` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_details`
--

DROP TABLE IF EXISTS `stock_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_details` (
  `CODE` varchar(100) DEFAULT NULL,
  `NAME` varchar(250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_signal_details`
--

DROP TABLE IF EXISTS `stock_signal_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_signal_details` (
  `CODE` varchar(100) DEFAULT NULL,
  `SIGNAL_DATE` date DEFAULT NULL,
  `PREVIOUS_SIGNAL_TYPE` varchar(15) DEFAULT NULL,
  `SIGNAL_TYPE` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `suggestions_data`
--

DROP TABLE IF EXISTS `suggestions_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `suggestions_data` (
  `CODE` varchar(100) DEFAULT NULL,
  `SUGGESTION_TYPE` varchar(10) DEFAULT NULL,
  `SIGNAL_PRICE` double DEFAULT NULL,
  `SIGNAL_TYPE` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `suggestions_data_new`
--

DROP TABLE IF EXISTS `suggestions_data_new`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `suggestions_data_new` (
  `CODE` varchar(100) DEFAULT NULL,
  `SUGGESTION_TYPE` varchar(10) DEFAULT NULL,
  `SIGNAL_PRICE` double DEFAULT NULL,
  `SIGNAL_TYPE` varchar(15) DEFAULT NULL,
  `SIGNAL_DATE` date DEFAULT NULL,
  `STOP_LOSS_PRICE` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_details`
--

DROP TABLE IF EXISTS `user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_details` (
  `USER_ID` varchar(25) DEFAULT NULL,
  `USERNAME` varchar(25) DEFAULT NULL,
  `MAIL` varchar(50) DEFAULT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `PASSWORD` varchar(100) DEFAULT NULL,
  `PLAN` varchar(20) DEFAULT NULL,
  `USERTYPE` varchar(10) DEFAULT NULL,
  `ACTIVE` varchar(2) DEFAULT NULL,
  `ACTIVATION_CODE` varchar(20) DEFAULT NULL,
  `CREATION_DATE` date DEFAULT NULL,
  `RENEWAL_DATE` date DEFAULT NULL,
  `EXPIRY_DATE` date DEFAULT NULL,
  `SEND_NOTIFICATION` tinyint(1) NOT NULL DEFAULT '0',
  `LAST_LOGIN_DATE` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_simulation_details`
--

DROP TABLE IF EXISTS `user_simulation_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_simulation_details` (
  `USER_ID` varchar(25) DEFAULT NULL,
  `CODE` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_trading_account_details`
--

DROP TABLE IF EXISTS `user_trading_account_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_trading_account_details` (
  `USER_ID` varchar(25) DEFAULT NULL,
  `CODE` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `watch_list`
--

DROP TABLE IF EXISTS `watch_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `watch_list` (
  `USER_ID` varchar(25) DEFAULT NULL,
  `CODE` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `world_stock_details`
--

DROP TABLE IF EXISTS `world_stock_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `world_stock_details` (
  `CODE` varchar(100) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `world_stock_price`
--

DROP TABLE IF EXISTS `world_stock_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `world_stock_price` (
  `CODE` varchar(100) DEFAULT NULL,
  `PRICE` double DEFAULT NULL,
  `CP` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-22 19:13:46
