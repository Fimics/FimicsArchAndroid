/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import BrowsingPage from './js/page/BrowsingPage';
import {name as appName} from './app.json';
import AsyncStorageDemo from './js/playground/AsyncStorageDemo';

AppRegistry.registerComponent(appName, () => AsyncStorageDemo);
