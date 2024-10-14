# ETL
```sql
CREATE TABLE dimclients (
    idclient  SERIAL PRIMARY KEY,
    country   VARCHAR(255),
    job_title VARCHAR(255),
    gender    VARCHAR(10)
);
```
```sql
CREATE TABLE dimfechas (
    idfecha SERIAL PRIMARY KEY,
    anio    INTEGER,
    mes     INTEGER
);
```
```sql
CREATE TABLE dimproducts (
    idproduct SERIAL PRIMARY KEY,
    product   VARCHAR(255)
);
```
```sql
CREATE TABLE factsales (
    idfecha   INTEGER REFERENCES dimfechas,
    idclient  INTEGER REFERENCES dimclients,
    idproduct INTEGER REFERENCES dimproducts,
    sale_paid NUMERIC,
    articles  NUMERIC,
    codigoventa INTEGER
);
```


![DWHsales](https://github.com/user-attachments/assets/bbc7aa71-4ccc-4d54-86bd-976531f319e4)
