# Frontend

The frontend is built using Angular 4.4.x (TypeScript). Angular requires a full nodejs installation for dependency management and development tooling.

## Installation

### nodejs

First, install nodejs and update its package manager npm.

```
curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
sudo apt-get install -y nodejs build-essential
sudo npm install -g npm
```

### angular-cli

The angular-cli utility is a node module that is required for the development of many angular application.
It is recommended to install this dependency globally. In the following we describe all necessary steps.

First, you need to set up the global npm module directory:

```
mkdir ~/.npm-global
npm config set prefix '~/.npm-global'
```

Add this to your .bashrc (or similar):

```
export PATH=~/.npm-global/bin:$PATH
```

Afterwards:

```
source ~/.bashrc
```

Finally, the angular cli tool will be installed:

```
npm install -g @angular/cli

```

If permission errors are encountered take a look at [npmjs - How to Prevent Permissions Errors](https://docs.npmjs.com/getting-started/fixing-npm-permissions).


## Running the frontend

Start a lite server for development purposes:

```
cd frontend/crypto-news-docs-app
ng serve
```

The frontend is reachable now at: `http://localhost:4200/`.
