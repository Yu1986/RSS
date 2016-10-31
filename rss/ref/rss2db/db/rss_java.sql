-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 13, 2012 at 07:56 PM
-- Server version: 5.5.22
-- PHP Version: 5.3.10-1ubuntu3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `rss`
--

-- --------------------------------------------------------

--
-- Table structure for table `news`
--

CREATE TABLE IF NOT EXISTS `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` text COLLATE utf8_bin NOT NULL,
  `source` text COLLATE utf8_bin NOT NULL,
  `title` text COLLATE utf8_bin NOT NULL,
  `content` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=37 ;

--
-- Dumping data for table `news`
--

INSERT INTO `news` (`id`, `url`, `source`, `title`, `content`) VALUES
(21, 'http://www.nasdaq.com/article/softs-highlights-top-stories-of-the-day-20120511-00945', 'bing.com', 'SOFTS HIGHLIGHTS: Top Stories Of The Day - NASDAQ', 'ISO Ups 2011-12 World Sugar Surplus View To 6.48 Mln Tons LONDON -(Dow Jones)- The International Sugar Organization Friday upwardly revised its estimate of the world sugar surplus for 2011-12, to 6.48 million metric tons from the 5.17 million ...'),
(22, 'http://blogs.wsj.com/indiarealtime/2012/05/10/top-stories-from-the-wall-street-journal-151/', 'bing.com', 'Top Stories From The Wall Street Journal - Wall Street Journal', 'Here is a daily roundup of some of the most important stories from The Wall Street Journal published on India.wsj.com. India, Iran Sign Trade Deals: Iran has sealed deals to buy shipments of rice, sugar and soybean from India as part of a plan ...'),
(23, 'http://www.mediabistro.com/galleycat/literary-barack-obama-book-design-tips-top-stories-of-the-week_b51482', 'bing.com', 'Literary Barack Obama & Book Design Tips: Top Stories of the Week - Mediabistro.com', 'For your weekend reading pleasure, here are our top stories of the week, including a 125-year wait for a book deal, the most read books in the world and book design tips from Chip Kidd (embedded above). Click here to sign up for GalleyCat’s daily email ...'),
(24, 'http://www.rawstory.com/rs/2012/05/11/top-romney-aide-outed-transgender-woman-in-political-smear/', 'bing.com', 'Top Romney aide outed transgender woman in political smear - Raw Story', 'Eric Fehrnstrom, a top aide and political strategist to presumptive Republican ... physical and economic survival,” Keisling told Raw Story, “Once you out a trans person, you can’t just ‘Etch a Sketch’ it away.”'),
(25, 'http://www.bloomberg.com/news/2012-05-13/rangers-top-capitals-2-1-advance-to-eastern-conference-finals.html', 'bing.com', 'Rangers Top Capitals 2-1, Advance to Eastern Conference Finals - Bloomberg', 'Michael Del Zotto scored the game- winning goal midway through the final period as the top-seeded New York Rangers defeated ... To contact the reporter on this story: Nancy Kercheval in Washington at nkercheval@bloomberg.net To contact the ...'),
(26, 'http://www.nasdaq.com/article/base-metals-highlights-top-stories-of-the-day-20120511-00680', 'bing.com', 'BASE METALS HIGHLIGHTS: Top Stories Of The Day - NASDAQ', 'Cape Lambert Agrees To Sell Australian Copper Project For A$25 Mln MELBOURNE -(Dow Jones)- Cape Lambert Resources Ltd. (CFE.AU) said Friday that it has reached an agreement to sell a copper project in Australia for A$25 million in cash, and will ...'),
(27, 'http://www.msnbc.msn.com/id/47398003', 'bing.com', 'Top 5 Stories on INCnow.tv for May 5th- 11th - msnbc.com', 'Welcome to the Top 5 for the week of May 5th- 11th. Below are brief summaries and links to the five most-viewed stories on INCnow.tv over the past week. Click on the links to view the stories. 1. Unbelievable! Kids on Hood of Moving Car Fort Wayne, IN ...'),
(28, 'http://mashable.com/2012/05/04/john-mcafee-arrested-linkedin-slideshare/', 'bing.com', 'Today’s Top Stories: John McAfee Arrested, LinkedIn to Buy SlideShare - Mashable.com', 'Welcome to this morning’s edition of “First To Know,” a series in which we keep you in the know on what’s happening in the digital world. Today, we’re looking at three particularly interesting stories. LinkedIn to Buy SlideShare For $ ...'),
(29, 'http://www.antimusic.com/news/12/May/ts11Rolling_Stones_Holding_Top_Secret_Rehearsals_.shtml', 'bing.com', 'Rolling Stones Holding Top Secret Rehearsals? (A Top Story) - antiMUSIC', 'On Friday Rolling Stones Holding Top Secret Rehearsals? was a top story. Here is the recap: (Gibson) A little rumor has hit the press that The Rolling Stones are currently in New York City, holding top-secret rehearsals. The Daily Mirror is reporting that ...');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
