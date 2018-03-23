import { Grid } from 'material-ui';
import React from 'react';
import { Link } from 'react-router';
import './HomePage.less';
import IconWalletNext from '../../images/icons/icon-wallet-next.png';
import IconGoogle from '../../images/g-logo.png';
import IconPiggyBank from '../../images/icons/icon-piggy-bank.png';
import IconInterface from '../../images/icons/icon-interface.png';
import IconBlockchain from '../../images/icons/icon-blockchain.png';
import './InfoContainer.less';

const HomePage = () => (
  <div className="home-page">
    <div className="container">
      <div className="main-icon"><img className="logo" src={IconWalletNext} alt="HUT34 Wallet"/></div>
      <h1 className="display-1"><strong>Sign in</strong></h1>
      <Link className="google-login" to="/login/google" target="_self"><img className="google-icon" src={IconGoogle} alt="G"/> Sign in using Google</Link>
    </div>


    <Grid container spacing={0} justify="center">
      <Grid item xs={12} sm={10} md={8}>
        <div className="info-container">
          <div className="info-title">
            Before you access your wallet
          </div>
          <div className="info-description">
            We know small print can be annoying, but we have some <span className="emphasis">very important </span>
            info and reminders about the Hut34 Wallet, and crypto wallets and crypto currency in general.
            <span className="emphasis">This is for your own safety</span> and peace of mind. Your crypto currency
            (which we will call “funds”) could be stolen or lost if you do not follow these warnings.
          </div>
        </div>
      </Grid>
    </Grid>
    <Grid container spacing={0} justify="center">
      <Grid item xs={12} sm={10} md>
        <div className="info-container">
          <div className="info-icon">
            <img src={IconPiggyBank} alt="bank"/>
          </div>
          <div className="info-title">
            <span>We&apos;re not a bank</span>
            <span className="sub-heading">or financial services licensee</span>
          </div>
          <div className="info-description">
            <p>
              We are providing services to help you create and manage your own Ethereum addresses. You, and only you,
              will have access to any tokens or ether contained within these wallets and, unlike a bank or a vault,
              we can&apos;t help you recover your funds if, for example, you set a password which you later mislay
              or you let others access.
            </p>
            <p>
              Always be thoughtful and careful, and remember that you are ultimately responsible for your own data.
            </p>
          </div>
        </div>
      </Grid>
      <Grid item xs={12} sm={10} md>
        <div className="info-container">

          <div className="info-icon">
            <img src={IconInterface} alt="interface"/>
          </div>
          <div className="info-title">
            It&apos;s an interface
          </div>
          <div className="info-description">
            <p>
              The Hut34 Wallet is an interface to the Ethereum blockchain. When you press &apos;send&apos;, for example, in this
              application, we forward those details, in the form of a transaction, to a node on the blockchain, from
              where it will be &apos;mined&apos;. It then becomes part of the immutable ledger, and a piece of Ethereum history.
            </p>
            <p>
              We do not, and can not, influence the blockchain once we have submitted a transaction; in fact, it would
              be very bad if we could.
            </p>
          </div>
        </div>
      </Grid>
      <Grid item xs={12} sm={10} md>
        <div className="info-container">
          <div className="info-icon">
            <img src={IconBlockchain} alt="blockchain"/>
          </div>
          <div className="info-title">
            What is a blockchain?
          </div>
          <div className="info-description">
            <p>
              A blockchain is simply a &apos;chain&apos;, or list, of &apos;blocks&apos;. There are some famous ones such as the
              Bitcoin blockchain, and the Etheruem blockchain, and there are many (many) others. The Ethereum blockchain, like
              Bitcoin, is a &apos;peer to peer&apos; network, meaning that lots of computers (nodes) exchange data, and each keeps
              a copy of the &apos;distributed ledger&apos;, being all of the programs and balances within the system.
            </p>
            <p>
              Blockchains typically use cryptography to ensure all records are up to date, and incredibly powerful outcomes flow
              from these simple premises. We love blockchains.
            </p>
          </div>
        </div>
      </Grid>
    </Grid>
  </div>
);

export default HomePage;
