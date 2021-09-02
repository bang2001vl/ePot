USE [master]
GO

/****** Object:  Database [FirstDB]    Script Date: 02/09/2021 9:06:11 SA ******/
CREATE DATABASE [FirstDB]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'FirstDB', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.SQLEXPRESS1\MSSQL\DATA\FirstDB.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'FirstDB_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.SQLEXPRESS1\MSSQL\DATA\FirstDB_log.ldf' , SIZE = 3136KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO

IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [FirstDB].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO

ALTER DATABASE [FirstDB] SET ANSI_NULL_DEFAULT OFF 
GO

ALTER DATABASE [FirstDB] SET ANSI_NULLS OFF 
GO

ALTER DATABASE [FirstDB] SET ANSI_PADDING OFF 
GO

ALTER DATABASE [FirstDB] SET ANSI_WARNINGS OFF 
GO

ALTER DATABASE [FirstDB] SET ARITHABORT OFF 
GO

ALTER DATABASE [FirstDB] SET AUTO_CLOSE OFF 
GO

ALTER DATABASE [FirstDB] SET AUTO_SHRINK OFF 
GO

ALTER DATABASE [FirstDB] SET AUTO_UPDATE_STATISTICS ON 
GO

ALTER DATABASE [FirstDB] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO

ALTER DATABASE [FirstDB] SET CURSOR_DEFAULT  GLOBAL 
GO

ALTER DATABASE [FirstDB] SET CONCAT_NULL_YIELDS_NULL OFF 
GO

ALTER DATABASE [FirstDB] SET NUMERIC_ROUNDABORT OFF 
GO

ALTER DATABASE [FirstDB] SET QUOTED_IDENTIFIER OFF 
GO

ALTER DATABASE [FirstDB] SET RECURSIVE_TRIGGERS OFF 
GO

ALTER DATABASE [FirstDB] SET  DISABLE_BROKER 
GO

ALTER DATABASE [FirstDB] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO

ALTER DATABASE [FirstDB] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO

ALTER DATABASE [FirstDB] SET TRUSTWORTHY OFF 
GO

ALTER DATABASE [FirstDB] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO

ALTER DATABASE [FirstDB] SET PARAMETERIZATION SIMPLE 
GO

ALTER DATABASE [FirstDB] SET READ_COMMITTED_SNAPSHOT OFF 
GO

ALTER DATABASE [FirstDB] SET HONOR_BROKER_PRIORITY OFF 
GO

ALTER DATABASE [FirstDB] SET RECOVERY SIMPLE 
GO

ALTER DATABASE [FirstDB] SET  MULTI_USER 
GO

ALTER DATABASE [FirstDB] SET PAGE_VERIFY CHECKSUM  
GO

ALTER DATABASE [FirstDB] SET DB_CHAINING OFF 
GO

ALTER DATABASE [FirstDB] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO

ALTER DATABASE [FirstDB] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO

ALTER DATABASE [FirstDB] SET  READ_WRITE 
GO


