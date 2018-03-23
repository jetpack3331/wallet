import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Grid } from 'material-ui';
import AcceptTermsForm from '../components/forms/AcceptTermsForm';
import { acceptTerms, logoutToHome } from '../actions/auth';
import './FirstLoginPage.less';
import './InfoContainer.less';

import IconWalletLock from '../../images/icons/icon-wallet-lock.png';
import IconPhishing from '../../images/icons/icon-phishing.png';
import IconScam from '../../images/icons/icon-scam.png';
import IconAvoidLoss from '../../images/icons/icon-avoid-loss.png';

const FirstLoginPage = ({ onAcceptTerms, onRejectTerms }) => (
  <div className="wallet-container first-login-page">
    <div className="container">
      <div className="widgets">
        <div className="container">

          <Grid container spacing={0} justify="center">
            <Grid item xs={12} sm={10} md={8}>
              <div className="info-container">
                <div className="info-icon">
                  <img src={IconWalletLock} alt="locked wallet"/>
                </div>
                <div className="info-title">
                  Safety and Security
                </div>
                <div className="info-description">
                  It&apos;s important you understand the nature of the Hut34 Wallet,
                  in order to make the best safety and security decisions for
                  you as an individual.
                  All addresses you create within the wallet are encrypted,
                  and stored in the Google Cloud Platform as associated with
                  your Google account. We utilise enterprise grade security
                  in the handling of all data, and you, and only you, are able
                  to decrypt the keys to each address. We talk about this a lot,
                  get in touch to learn more.
                </div>
              </div>
            </Grid>
          </Grid>
          <Grid container spacing={0} justify="center">
            <Grid item xs={12} sm={10} md>
              <div className="info-container">
                <div className="info-icon">
                  <img src={IconPhishing} alt="phishing"/>
                </div>
                <div className="info-title">
                  You are responsible
                </div>
                <div className="info-description">
                  The security of addresses you create within a Hut34 Wallet is linked
                  to the security of your Google account. We strongly recommend that you
                  follow the good advice at https://myaccount.google.com/security, and
                  setup &apos;2FA&apos;, and other security measures to protect your account.

                  Google will also prompt you regularly to complete a security checklist,
                  which is a powerful tool to ensure your security remains strong, and up to date.
                </div>
              </div>
            </Grid>
            <Grid item xs={12} sm={10} md>
              <div className="info-container">

                <div className="info-icon">
                  <img src={IconScam} alt="scam"/>
                </div>
                <div className="info-title">
                  Protect from Scams
                </div>
                <div className="info-description">
                  Always keep in mind that once you send a transaction to the Ethereum blockchain,
                  you will not be able to undo it. We support decentralised &apos;trustless&apos;
                  models wherever possible that avoid you having to trust any third party with your
                  tokens.

                  The Hut34 Wallet is a tool to make your life a little easier when interacting
                  with the Ethereum blockchain, but always be thoughtful and careful.
                </div>
              </div>
            </Grid>
            <Grid item xs={12} sm={10} md>
              <div className="info-container">
                <div className="info-icon">
                  <img src={IconAvoidLoss} alt="avoid loss"/>
                </div>
                <div className="info-title">
                  So what&apos;s the point?
                </div>
                <div className="info-description">
                  Your Hut34 Wallet will allow you to create addresses incredibly quickly and
                  easily, and be confident that they are stored securely within the
                  infrastructure of the Google cloud platform.

                  You may wish to download your keystore and private keys to keep offline
                  backups, or you may wish to transfer larger value amounts to other storage
                  devices. The point is that it&apos;s up to you. Enjoy being part of the
                  Ethereum blockchain!
                </div>
              </div>
            </Grid>
          </Grid>

          <div className="terms-container">
            <AcceptTermsForm onSubmit={onAcceptTerms} onCancel={onRejectTerms}/>
          </div>

        </div>
      </div>
    </div>
  </div>
);

FirstLoginPage.propTypes = {
  onAcceptTerms: PropTypes.func.isRequired,
  onRejectTerms: PropTypes.func.isRequired,
};

const mapDispatchToProps = {
  onAcceptTerms: acceptTerms,
  onRejectTerms: logoutToHome,
};

export default connect(null, mapDispatchToProps)(FirstLoginPage);
