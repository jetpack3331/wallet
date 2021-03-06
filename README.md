# Hut34 Wallet

This project aims to make wallets easier for humans and bots. The codebase is still very
much evolving, so stay tuned!
 
## Technical info

This project was generated using the [React Spring GAE generator][generator-spring-gae-react]. 

### Prerequisites

Before you can run this app up you'll need to check you have the following tools:

* Java 8
* Maven 3
* Node >= 6.9.1

### Getting Started

To develop the application you'll need to run two processes. 1) The GAE dev server which runs your 
server-side code and 2) the Webpack dev server which offers nice things like live updating and
hot module reloading. 

1. **Start the App Engine dev server**

  `mvn appengine:run`

  You can check the server is running by opening [localhost:8080](http://localhost:8080). 
  
  > Note: the static resources served up here will not be updated unless you run `npm run build` or
  > `mvn install` as the Webpack dev server operates entirely in memory.

2. **In a separate terminal window/tab start the Webpack dev server**

  `npm start`

  The Webpack dev server starts on port 3000. Open [localhost:3000](http://localhost:3000) in your
  browser and you should be good to go.

### Deploying

Before deploying you will need privileged environment-specific configuration which cannot be shared
on a public repository for obvious security reasons. These are maintained separately and we create symlinks
in `src/main/resources` on our local drive for the following files (these are in `.gitignore`):

* application-hut34-wallet-local.yaml
* application-hut34-wallet-dev.yaml 
* application-hut34-wallet-uat.yaml
* application-hut34-wallet-prod.yaml

When you're ready to deploy your application you can:

1. **Package your app for deployment**

  `mvn package`

2. **Deploy to App Engine**

  `mvn appengine:update -P<Environment Name>`

  Where environment name matches a profile in your `pom.xml`.

[generator-spring-gae-react]: https://github.com/3wks/generator-spring-gae-react
