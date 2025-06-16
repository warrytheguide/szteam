# Alkalmazás futtatása Dockeren keresztül:

## Klónozás és megfelelő branchre váltás:

    git clone https://github.com/warrytheguide/szteam.git
    
    git checkout working-no-liquid
    

## Docker imagek előállítása és futtatása:

 ### szteam\szteam-network directoryból:
 

    docker build -t szt/szt:1.0.0 -f ../docker/backend/Dockerfile . 

### szteam\szteam-network-frontend directoryból:

    docker build -t szt/szt-frontend:1.0.0 -f ../docker/frontend/Dockerfile .

	docker compose up

## Alkalmazás elérési útvonala:
	

http://localhost:8080/register
