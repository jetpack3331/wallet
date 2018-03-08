import { Grid } from 'material-ui';
import React from 'react';
import { walletAccount } from '../../model';
import DownloadKeystoreButton from './DownloadKeystoreButton';
import WalletIcon from '../../../images/icon-wallet.png';

const MyWallet = props => (
  <Grid container spacing={24}>
    <Grid item xs={12} sm={6}>
      <img src={WalletIcon} className="icon-wallet" alt="Wallet icon"/>
      <h1 className="display-1 inline-title"><strong>My Wallet</strong></h1>
    </Grid>
    <Grid item xs={12} sm={6}>
      <DownloadKeystoreButton walletAccount={props.walletAccount}/>
    </Grid>
    <Grid item xs={12}>
      <p>Wallet address: {props.walletAccount.address}</p>
    </Grid>
  </Grid>
);

MyWallet.propTypes = {
  walletAccount: walletAccount.isRequired,
};


export default MyWallet;
