INSERT INTO `degree_programmes` (`id`, `name`) VALUES
(1, 'Ingegneria Informatica'),
(2, 'Computer Engineering'),
(3, 'Artificial Intelligence and Data Engineering'),
(4, 'Ingegneria Meccanica'),
(5, 'Ingegneria Gestionale'),
(6, 'Bionics Engineering');



INSERT INTO `users` (`Id`, `Username`, `Password`, `degreeId`, `Admin`) VALUES
(1, 'Geghi', 'Geghi', 3, 1),
(2, 'alice', 'alice', 3, 0),
(4, 'stefano', 'stefano', 3, 0),
(6, 'marco', 'marco', 3, 0),
(8, 'mattew', 'mattew', 6, 0);



INSERT INTO `professors` (`Id`, `name`, `surname`, `info`) VALUES
(1, 'Giuseppe', 'Lettieri', 'Professore associato presso l\'Università di Pisa. Il 22.7.1998 ha conseguito la laurea in Ingegneria Informatica, con lode, presso l’Università degli studi di Pisa. Il 14.3.2002 ha ricevuto il titolo di Dottore di Ricerca in Ingegneria dell’Informazione, discutendo la Tesi “Progetto e Realizzazione di un Sistema Distribuito a Spazio Singolo”. '),
(2, 'Luigi', 'Berselli', 'Professore di analisi matematica presso il dipartimento di Matematica dell\'Università di Pisa. Lareato con lode in Matematica presso la scuola normale superiore nel 1995, consegue la laurea magistrale presso la scuola normale superiore di Pisa nel 1997. Dal 2016 è professore ordinario presso l\'Università di Pisa.'),
(3, 'Marco', 'Cococcioni', 'Professore associato presso il Dipartimento di Ingegneria dell\'Informazione (Università di Pisa) e membro del laboratorio del laboratorio del Data Science and Engineering. '),
(4, 'Giuseppe', 'Anastasi', 'Professore del Dipartimento di Ingegneria dell\'Informazione dell\'Università di Pisa. Nel 1990 ha conseguito la laurea in Ingegneria Elettronica e nel 1995 ha ottenuto il dottorato in Ingegneria Informatica. Si occupa di ricerca nell\'ambito di Internet delle cose e Sustainable Computing'),
(5, 'Francesco', 'Marcelloni', 'Francesco Marcelloni received the Laurea degree in Electronics Engineering and the Ph.D. degree in Computer Engineering from the University of Pisa in 1991 and 1996, respectively. He is currently a full professor at the Department of Information Engineering of the University of Pisa and Vice Rector for International Cooperation and Relations. He has been a member of the Academic Senate of the University of Pisa. He has co-founded the Computational Intelligence Group at the Department of Information Engineering of the University of Pisa in 2002. Further, he is the founder and head of the Competence Centre on MObile Value Added Services (MOVAS). '),
(6, 'Giovanni', 'Stea', 'Giovanni Stea received the Laurea degree (summa cum laude) and the PhD degree in Computer Systems Engineering from the University of Pisa, Italy, in 1999 and 2003 respectively. Starting from January 2004, he has been with the Department of Information Engineering, where he is now Associate Professor and responsible for the research activities of the Networking and Cloud Computing Lab. '),
(7, 'Mario G.C.A.', 'Cimino', ' Mario G.C.A. Cimino is an Associate Professor at the Department of Information Engineering of the University of Pisa (Italy). He received the Ph.D. degree in Information Engineering from the University of Pisa in 2007. From 2003 to 2006, as a Ph.D. student he joined the Department of Information Engineering of the University of Pisa, working on computational intelligence and information systems. Since April 2006, he spent six months as a visiting scholar at the Electrical & Computer Engineering Department of the University of Alberta, Edmonton (Canada), under the supervision of Prof. W. Pedrycz, for a research activity on neurocomputing and granular computing.'),
(8, 'Alessandro', 'Paoli', 'Professore Associato presso il Dipartimento di Ingegneria Civile e Industriale\r\nSettore scientifico disciplinare: Disegno e Metodi dell\'Ingegneria Industriale ING-IND/15'),
(9, 'Leonardo', 'Bertini', 'Nato a Pisa nel 1954, il professore Leonardo Bertini si è laureato in Ingegneria nucleare presso l’Università di Pisa nel 1980. Dopo tre anni di lavoro in aziende nazionali operanti nel settore meccanico, nel 1986 ha conseguito il dottorato in Meccanica dei Materiali. Dal 1988 si è quindi inserito nel personale di ruolo dell’Ateneo, diventando professore associato in Progettazione meccanica e Costruzione di Macchine e poi professore ordinario nel 2000.');


INSERT INTO `subjects` (`Id`, `name`, `credits`, `degreeId`, `info`) VALUES
(1, 'Analisi Matematica I', 12, 1, 'Fornire conoscenze di base sulla teoria delle funzioni di una variabile reale: struttura dei numeri reali, continuità, limiti, calcolo differenziale ed integrale, sull\'algebra dei numeri complessi, sulla teoria elementare delle equazioni differenziali e delle serie numeriche e di potenze.'),
(2, 'Fondamenti di informatica I', 12, 1, 'Fornire le basi della rappresentazione dell’informazione e della programmazione (a livello macchina, strutturata ed ad oggetti), per passare da un problema ad un procedimento risolutivo ed al successivo programma. I linguaggi utilizzati sono: GNU Assembler, C e C++.'),
(3, 'Reti informatiche', 9, 1, 'L\'insegnamento ha lo scopo di fornire le conoscenze di base sulle reti di calcolatori , sulla programmazione di applicazioni distribuite e sull\'amministrazione di un sistema informatico in rete.'),
(4, 'Calcolatori elettronici', 9, 1, 'L’insegnamento ha l’obiettivo di fornire conoscenze sull’architettura dei moderni calcolatori, sulla organizzazione delle interfacce comunemente utilizzate e sul nucleo di sistema operativo.'),
(5, 'Costruzine di Macchine', 12, 4, 'Il corso fornisce agli studenti le conoscenze e di strumenti operativi necessari per eseguire la progettazione meccanica avanzata di un sistema meccanico (meccanico o macchina completa). Al termine del corso l\'allievo sarà in grado di eseguire, con procedimento manuale o con l\'ausilio del calcolatore, l\'analisi del comportamento di un sistema meccanico nelle varie condizioni operative nelle quali tale sistema dovrà funzionare.'),
(6, 'Programmazione Avanzata', 6, 1, 'Il corso si propone di fornire conoscenze e competenze nell’ambito della programmazione a oggetti, con particolare riferimento a sistemi concorrenti e distribuiti. I problemi fondamentali derivanti da una esecuzione parallela vengono affrontati in modo pratico usando il linguaggio Java. I principali aspetti progettuali e realizzativi legati ad applicazioni software composte da più sottosistemi vengono sperimentati attraverso la realizzazione di un progetto didattico usando il linguaggio Java.'),
(7, 'Reti Logiche', 9, 1, 'L’insegnamento ha l’obiettivo di fornire le conoscenze di base sulle reti logiche e sulla architettura dei calcolatori.'),
(8, 'Performance Evaluation of Computer Systems and Networks', 9, 2, 'La padronanza del calcolo delle probabilita\' e delle tecniche analitiche di modellazione sara\' verificata attraverso esercizi scritti, che richiedono che uno studente modelli in modo appropriato una situzione descritta, che ricavi misure di prestazioni dai dati di ingresso, e che spieghi i risultati. \r\nLa padronanza nella simulazione sara\' verificata analizzando il codice ed i risultati di un progetto di gruppo, che consiste nella modellazione e simulazione di un sistema semplice, e nell\'analisi delle prestazioni di quest\'ultimo. \r\nLa padronanza delle tecniche di analisi statistica verra\' verificata attraverso esercizi individuali dati agli studenti durante le ore di esercitazione. '),
(9, 'Progettazione Web', 6, 1, 'L\'insegnamento ha l\'obiettivo di introdurre alle metodologie per lo sviluppo di applicazioni Web con particolare riferimento a protocolli, linguaggi e tecnologie allo stato dell’arte.'),
(10, 'Data Mining and Machine Learning', 12, 3, 'Il corso si propone di introdurre i concetti e le tecniche principali usate in data mining e machine learning per estrarre conoscenza dai dati. In particolare, il corso si concentrerà sugli aspetti seguenti: pre-processazione dei dati, estrazione dei pattern frequenti, estrazione dei pattern sequenziali, classificazione, predizione, clustering di oggetti e grafi, rilevazione di anomalie, analisi di flussi di dati, data mining distribuito. Il corso tratterà in sequenza gli aspetti seguenti relativi a data mining e machine learning. Analisi preliminare e visualizzazione dei dati. Pre-processazione dei dati. Estrazione dei pattern frequenti e regole associative. Classificazione. Clustering. Rilevazione di anomalie. Clustering di grafi. Estrazione di pattern sequenziali frequenti. Data mining di flussi di dati. Framework per data mining distribuito.');



INSERT INTO `prof_comments` (`id`, `userId`, `profId`, `text`, `date`) VALUES
(1, 1, 4, 'Il professore non è molto disponibile', '2019-10-09 13:44:41'),
(11, 2, 5, '', '2019-10-24 08:48:42'),
(16, 2, 5, '', '2019-10-24 09:00:43'),
(18, 1, 5, 'geghi', '2019-10-24 09:05:41'),
(19, 1, 5, 'geghi', '2019-10-24 09:05:42'),
(20, 2, 5, 'Leave a comment here...', '2019-10-24 09:05:52'),
(26, 1, 5, 'Leave a comment here...', '2019-10-24 12:47:04'),
(34, 1, 5, 'provo un testo lungo e vedo cosa succede\nprovo un testo lungo e vedo cosa succedeprovo un testo lungo e vedo cosa succedeprovo un testo lungo e vedo cosa succede\nprovo un testo lungo e vedo cosa succede\nprovo un testo lungo e vedo cosa succedeprovo un testo lungo e vedo cosa succede\nprovo un testo lungo e vedo cosa succede\nprovo un testo lungo e vedo cosa succede\nprovo un testo lungo e vedo cosa succede\n', '2019-10-24 14:47:05');



INSERT INTO `subject_comments` (`id`, `userId`, `subjectId`, `text`, `date`) VALUES
(1, 1, 1, 'Spiega bene ma va troppo veloce a mio parere', '2019-10-09 13:25:57'),
(2, 1, 4, 'L\'esame è molto difficile ma le spiegazioni sono molto precise. Da migliorare forse la preparazione in laboratiori.', '2019-10-09 13:27:13'),
(3, 1, 10, 'Leave a comment here...xdddd', '2019-10-23 19:21:30'),
(4, 1, 10, 'Leave a comment here...xdddd', '2019-10-23 19:21:44'),
(5, 1, 10, 'Leave a comment here...', '2019-10-23 19:29:41'),
(7, 1, 10, 'geghi		', '2019-10-24 09:14:51'),
(8, 1, 10, 'geghi', '2019-10-24 09:14:54'),
(12, 6, 10, '', '2019-10-24 09:20:40'),
(13, 2, 10, 'Leave a comment here...', '2019-10-24 09:31:01'),
(14, 2, 10, '', '2019-10-24 09:35:04'),
(15, 2, 10, '', '2019-10-24 09:35:04'),
(16, 2, 10, 'ciao', '2019-10-25 10:40:58');

INSERT INTO `teaching` (`profId`, `subjectId`) VALUES
(1, 4),
(2, 1),
(3, 2),
(4, 3),
(5, 9),
(5, 10),
(6, 7),
(7, 6),
(9, 5);

