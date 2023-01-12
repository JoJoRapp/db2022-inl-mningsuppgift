# db2022-inl-mningsuppgift
Slutprojekt i databas-kursen

## Entity Relationship Diagram

erDiagram
	STUDENT ||--o{ PHONE : has
	STUDENT ||--o{ STUDENTSCHOOL : attends
	STUDENT {
		int StudentId
		string Name
	}
	SCHOOL ||--o{ STUDENTSCHOOL : enrolls
	SCHOOL {
		int SchoolId
		string Name
		string City
	}
	PHONE {
		int PhoneId
		int StudentId
		tinyint IsHome
		tinyint IsJob
		tinyint IsMobile
		string Number
	}
	STUDENTSCHOOL {
		int StudentId
		int SchoolId
	}
