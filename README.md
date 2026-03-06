# File Storage API

Ett Spring Boot-baserat API som fungerar som ett enklare Google Drive / Dropbox.
Bygger på controller-service-repository-arkitektur och använder OAuth2/OpenID Connect
via GitHub för autentisering, med JWT som komplement.

## Funktioner

- Skapa ny mapp
- Ladda upp fil till specifik mapp
- Ladda ned fil
- Ta bort fil
- Registrera användare (via GitHub OAuth2)
- Logga in användare (via GitHub OAuth2)
- Alla mappar och filer kopplas till användare
- Användare kan inte se andra användares filer eller mappar
- HATEOAS-länkar i API-svar (RepresentationModel)
- Säkerhet med Spring Security

## Teknologier

- Spring Boot
- PostgreSQL
- Spring Data JPA
- Spring Security
- OAuth2 / OpenID Connect (GitHub)
- JWT (komplement till OAuth2)
- HATEOAS
- Gradle

## Autentisering

Applikationen stödjer två autentiseringsmetoder:

1. **GitHub OAuth2 (primär)** – Gå till `/oauth2/authorization/github` för att
   logga in eller registrera dig via GitHub. En JWT returneras automatiskt efter
   lyckad inloggning.

2. **Username/password med JWT (komplement)** – Registrera via `POST /users/register`
   och logga in via `POST /auth/login` för att få en JWT-token.

JWT-token används sedan i `Authorization: Bearer <token>`-headern för alla skyddade endpoints.