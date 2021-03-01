/**
 * @format
 */

import {AppRegistry} from 'react-native';
import React from 'react'
import {name as appName} from './app.json';
import RNBridgeDemo from "./js/page/RNBridgeDemo";
import BrowsingPage from './js/page/BrowsingPage'

const PageRoute = {
    '/browsing': BrowsingPage,
    '/bridgeDemo': RNBridgeDemo

};

class Index extends React.Component {
    getPageComponent() {
        const {routeTo} = this.props;
        const page = PageRoute[routeTo];
        return page
    }

    render(): React.ReactNode {
        const Page = this.getPageComponent();
        return <Page/>;
    }
}

//单RN实例的方式
// AppRegistry.registerComponent(appName, () => Index);
//多RN实例的方式
AppRegistry.registerComponent('rn_module_browsing', () => BrowsingPage);
AppRegistry.registerComponent('rn_module_bridgeDemo', () => RNBridgeDemo);
// AppRegistry.registerComponent('demo_page', () => DemoPage);
