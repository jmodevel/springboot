INSERT INTO publisher (id, name, headquarters, website)
VALUES
  (1, 'Alfaguara', 'Madrid', 'https://www.penguinlibros.com/es/11579-alfaguara' ),
  (2, 'DeBolsillo', 'Barcelona', 'https://www.penguinlibros.com/es/11338-debolsllo'),
  (3, 'Maxi-Tusquets', 'Barcelona', 'https://www.planeta.es/es/maxitusquets' ),
  (4, 'Editorial Empúries', 'Barcelona', 'https://www.planeta.es/es/editorial-emp%C3%BAries' ),
  (5, 'Tusquets Editores S.A.', 'Barcelona', 'https://www.planeta.es/es/tusquets-editores' )
ON DUPLICATE KEY UPDATE
  id           = VALUES(id),
  name         = VALUES(name),
  headquarters = VALUES(headquarters),
  website      = VALUES(website);

INSERT INTO author (id, name, surnames, birth_date, death)
VALUES
  (1, 'José', 'Saramago', '1922-11-16', '2010-06-18' ),
  (2, 'Mario', 'Benedetti', '1920-09-14', '2009-05-17'),
  (3, 'Haruki', 'Murakami', '1949-01-12', null)
ON DUPLICATE KEY UPDATE
  id         = VALUES(id),
  name       = VALUES(name),
  surnames   = VALUES(surnames),
  birth_date = VALUES(birth_date),
  death      = VALUES(death);

INSERT INTO book (id, title, isbn, pages, published, publisher, author)
VALUES
  (1, 'Ensayo sobre la ceguera', '9788490628720', 376, '2015-09-10', 1, 1 ),
  (2, 'Ensayo sobre la lucidez', '9788490628768', 384, '2015-09-17', 2, 1 ),
  (3, 'El viaje del elefante', '9788420474632', 280, '2008-11-12', 1, 1 ),
  (4, 'La tregua', '9788490626726', 208, '2015-07-02', 2, 2 ),
  (5, 'El amor, las mujeres y la vida', '9788420477343', 216, '2015-07-02', 1, 2 ),
  (6, 'Vivir adrede', '9788420473437', 160, '2008-01-22', 1, 2 ),
  (7, 'Tokio Blues', '9788483835043', 384, '2007-05-01', 3, 3 ),
  (8, 'Crónica del pájaro que da cuerda al mundo', '9788483835104', 912, '2008-01-01', 3, 3 ),
  (9, 'Primera personal del singular', '9788418833090', 216, '2021-09-29', 4, 3 ),
  (10, '1Q84', '9788483833551', 416, '2011-10-01', 5, 3 )
ON DUPLICATE KEY UPDATE
  id        = VALUES(id),
  title     = VALUES(title),
  isbn      = VALUES(isbn),
  pages     = VALUES(pages),
  published = VALUES(published),
  publisher = VALUES(publisher),
  author    = VALUES(author);