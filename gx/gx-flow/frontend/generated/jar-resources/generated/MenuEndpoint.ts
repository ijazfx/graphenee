/**
 * This module is generated from MenuEndpoint.java
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 * @see {@link file:///var/jenkins_home/workspace/AppLayout-5.x-addon/target/checkout/src/main/java/com/flowingcode/addons/applayout/endpoint/MenuEndpoint.java}
 * @module MenuEndpoint
 */

// @ts-ignore
import client from './connect-client.default';
// @ts-ignore
import { EndpointRequestInit, Subscription } from '@hilla/frontend';

import type MenuItemDto from './com/flowingcode/addons/applayout/endpoint/MenuItemDto';

function _getMenuItems(
 __init?: EndpointRequestInit
): Promise<Array<MenuItemDto>>
{
 return client.call('MenuEndpoint', 'getMenuItems', {}, __init);
}
export {
  _getMenuItems as getMenuItems,
};
