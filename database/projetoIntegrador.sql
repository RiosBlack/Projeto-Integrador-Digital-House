CREATE DATABASE projetoIntegrador;
USE projetoIntegrador; 

CREATE TABLE categories(
	idCategory BIGINT AUTO_INCREMENT, 
    kind VARCHAR(13) NOT NULL,
    qualification TINYINT NOT NULL, 
    datails VARCHAR(250) NOT NULL,
    imageURL VARCHAR(200),
    PRIMARY KEY (idCategory) 
);

CREATE TABLE features(
    idFeature BIGINT AUTO_INCREMENT, 
    featureTitle VARCHAR(50) NOT NULL,
    featureIconUrl VARCHAR(200),
    PRIMARY KEY (idFeature)
); 