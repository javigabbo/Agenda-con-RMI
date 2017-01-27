-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 27, 2017 at 09:40 AM
-- Server version: 10.1.16-MariaDB
-- PHP Version: 7.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `agenda`
--

-- --------------------------------------------------------

--
-- Table structure for table `contactos`
--

CREATE TABLE `contactos` (
  `username_fk` varchar(45) NOT NULL,
  `telefono` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contactos`
--

INSERT INTO `contactos` (`username_fk`, `telefono`, `nombre`, `email`) VALUES
('javi', 1, 'Anthony', 'antonioo'),
('javi', 2, 'Paula', 'pau@gmail.com'),
('pedro', 12, 'javi', 'javi@gmail.com'),
('mario', 12245, 'Lucas', 'lucas@gmail.com'),
('pedro', 12344, 'Maria', 'maria@gmail.com'),
('pedro', 12349, 'Mary', 'mary@gmail.com'),
('mario', 13245, 'Azucena', 'maria@gmail.com'),
('mario', 124556, 'Emilio', 'emilio@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `usuarios`
--

INSERT INTO `usuarios` (`username`, `password`) VALUES
('javi', 'javi'),
('mario', 'mario'),
('pedro', 'pedro');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `contactos`
--
ALTER TABLE `contactos`
  ADD PRIMARY KEY (`telefono`),
  ADD KEY `username_fk_idx` (`username_fk`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`username`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `contactos`
--
ALTER TABLE `contactos`
  ADD CONSTRAINT `username_fk` FOREIGN KEY (`username_fk`) REFERENCES `usuarios` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
