# Wissensgewinnung durch Verbindung von Kryptowährungsdaten mit korrespondierenden Nachrichten 

**Contributors**: David Drost, Hendrik Sawade, Christopher Schröder und Tobias Wenzel

**Description**: This project aims to generate knowledge about cryptocurrencies by mapping news onto exchange data. Hence the focus is on content aggregation, data integration and knowledge discovery. Moreover, a front displays the news articles, visualized the market data and shows a world map for breaking down information by geographical location.
## Overview ##

The project is divided into the following components:
- [backend](./backend),
- [crawler](./crawler),
- and [frontend](./frontend).

## Running the application ##

Each component contains a separate README file describing the processes of installing and running the respective components. 

The installation instructions are written for Linux Mint 18.3 (64 bit).
Before you start, make sure the following software is installed:

- Java 8 (JDK)
- Python 3.6+
- [Docker 17.05.0-ce](https://docs.docker.com/install/linux/docker-ce/ubuntu/)
- [Docker Compose](https://docs.docker.com/compose/install/)


After installing these prerequisites, execute the following steps. A detailed description for setting up each component is provided in the respective component's Readme file.

1. Start the [backend](./backend/README.md)
- At first, the docker container needs to be started. It provides a preconfigured postgres database.
- Afterwards you can start the Java application.

2. Run the [crawler](./crawler/README.md)s **`(*)`**
- The crawlers will fetch both news articles and market data (time series) of the respective cryptocurrencies.
- All this data is sent to the backend.

  `(*)` This step is optional if you imported the provided data during the backend installation.

3. Build the frontend and run the [frontend](./frontend/README.md) dev server
- The development dependencies which are used in this project provide a lightweight http server for development purposes.
- Once the server is running, the frontend is available at `http://localhost:4200/`.
