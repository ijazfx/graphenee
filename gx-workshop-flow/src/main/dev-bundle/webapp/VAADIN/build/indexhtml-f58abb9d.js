(function(){const e=document.createElement("link").relList;if(e&&e.supports&&e.supports("modulepreload"))return;for(const n of document.querySelectorAll('link[rel="modulepreload"]'))i(n);new MutationObserver(n=>{for(const s of n)if(s.type==="childList")for(const r of s.addedNodes)r.tagName==="LINK"&&r.rel==="modulepreload"&&i(r)}).observe(document,{childList:!0,subtree:!0});function o(n){const s={};return n.integrity&&(s.integrity=n.integrity),n.referrerPolicy&&(s.referrerPolicy=n.referrerPolicy),n.crossOrigin==="use-credentials"?s.credentials="include":n.crossOrigin==="anonymous"?s.credentials="omit":s.credentials="same-origin",s}function i(n){if(n.ep)return;n.ep=!0;const s=o(n);fetch(n.href,s)}})();window.Vaadin=window.Vaadin||{};window.Vaadin.featureFlags=window.Vaadin.featureFlags||{};window.Vaadin.featureFlags.exampleFeatureFlag=!1;window.Vaadin.featureFlags.collaborationEngineBackend=!1;window.Vaadin.featureFlags.webPush=!1;window.Vaadin.featureFlags.formFillerAddon=!1;const Cn="modulepreload",kn=function(t,e){return new URL(t,e).href},Yo={},g=function(e,o,i){if(!o||o.length===0)return e();const n=document.getElementsByTagName("link");return Promise.all(o.map(s=>{if(s=kn(s,i),s in Yo)return;Yo[s]=!0;const r=s.endsWith(".css"),l=r?'[rel="stylesheet"]':"";if(!!i)for(let h=n.length-1;h>=0;h--){const v=n[h];if(v.href===s&&(!r||v.rel==="stylesheet"))return}else if(document.querySelector(`link[href="${s}"]${l}`))return;const d=document.createElement("link");if(d.rel=r?"stylesheet":Cn,r||(d.as="script",d.crossOrigin=""),d.href=s,document.head.appendChild(d),r)return new Promise((h,v)=>{d.addEventListener("load",h),d.addEventListener("error",()=>v(new Error(`Unable to preload CSS for ${s}`)))})})).then(()=>e()).catch(s=>{const r=new Event("vite:preloadError",{cancelable:!0});if(r.payload=s,window.dispatchEvent(r),!r.defaultPrevented)throw s})};function bt(t){return t=t||[],Array.isArray(t)?t:[t]}function Q(t){return`[Vaadin.Router] ${t}`}function Tn(t){if(typeof t!="object")return String(t);const e=Object.prototype.toString.call(t).match(/ (.*)\]$/)[1];return e==="Object"||e==="Array"?`${e} ${JSON.stringify(t)}`:e}const wt="module",Et="nomodule",yo=[wt,Et];function Jo(t){if(!t.match(/.+\.[m]?js$/))throw new Error(Q(`Unsupported type for bundle "${t}": .js or .mjs expected.`))}function qi(t){if(!t||!J(t.path))throw new Error(Q('Expected route config to be an object with a "path" string property, or an array of such objects'));const e=t.bundle,o=["component","redirect","bundle"];if(!Ee(t.action)&&!Array.isArray(t.children)&&!Ee(t.children)&&!St(e)&&!o.some(i=>J(t[i])))throw new Error(Q(`Expected route config "${t.path}" to include either "${o.join('", "')}" or "action" function but none found.`));if(e)if(J(e))Jo(e);else if(yo.some(i=>i in e))yo.forEach(i=>i in e&&Jo(e[i]));else throw new Error(Q('Expected route bundle to include either "'+Et+'" or "'+wt+'" keys, or both'));t.redirect&&["bundle","component"].forEach(i=>{i in t&&console.warn(Q(`Route config "${t.path}" has both "redirect" and "${i}" properties, and "redirect" will always override the latter. Did you mean to only use "${i}"?`))})}function Xo(t){bt(t).forEach(e=>qi(e))}function Qo(t,e){let o=document.head.querySelector('script[src="'+t+'"][async]');return o||(o=document.createElement("script"),o.setAttribute("src",t),e===wt?o.setAttribute("type",wt):e===Et&&o.setAttribute(Et,""),o.async=!0),new Promise((i,n)=>{o.onreadystatechange=o.onload=s=>{o.__dynamicImportLoaded=!0,i(s)},o.onerror=s=>{o.parentNode&&o.parentNode.removeChild(o),n(s)},o.parentNode===null?document.head.appendChild(o):o.__dynamicImportLoaded&&i()})}function $n(t){return J(t)?Qo(t):Promise.race(yo.filter(e=>e in t).map(e=>Qo(t[e],e)))}function qe(t,e){return!window.dispatchEvent(new CustomEvent(`vaadin-router-${t}`,{cancelable:t==="go",detail:e}))}function St(t){return typeof t=="object"&&!!t}function Ee(t){return typeof t=="function"}function J(t){return typeof t=="string"}function Wi(t){const e=new Error(Q(`Page not found (${t.pathname})`));return e.context=t,e.code=404,e}const Pe=new class{};function An(t){const e=t.port,o=t.protocol,s=o==="http:"&&e==="80"||o==="https:"&&e==="443"?t.hostname:t.host;return`${o}//${s}`}function Zo(t){if(t.defaultPrevented||t.button!==0||t.shiftKey||t.ctrlKey||t.altKey||t.metaKey)return;let e=t.target;const o=t.composedPath?t.composedPath():t.path||[];for(let l=0;l<o.length;l++){const a=o[l];if(a.nodeName&&a.nodeName.toLowerCase()==="a"){e=a;break}}for(;e&&e.nodeName.toLowerCase()!=="a";)e=e.parentNode;if(!e||e.nodeName.toLowerCase()!=="a"||e.target&&e.target.toLowerCase()!=="_self"||e.hasAttribute("download")||e.hasAttribute("router-ignore")||e.pathname===window.location.pathname&&e.hash!==""||(e.origin||An(e))!==window.location.origin)return;const{pathname:n,search:s,hash:r}=e;qe("go",{pathname:n,search:s,hash:r})&&(t.preventDefault(),t&&t.type==="click"&&window.scrollTo(0,0))}const Rn={activate(){window.document.addEventListener("click",Zo)},inactivate(){window.document.removeEventListener("click",Zo)}},Nn=/Trident/.test(navigator.userAgent);Nn&&!Ee(window.PopStateEvent)&&(window.PopStateEvent=function(t,e){e=e||{};var o=document.createEvent("Event");return o.initEvent(t,!!e.bubbles,!!e.cancelable),o.state=e.state||null,o},window.PopStateEvent.prototype=window.Event.prototype);function ei(t){if(t.state==="vaadin-router-ignore")return;const{pathname:e,search:o,hash:i}=window.location;qe("go",{pathname:e,search:o,hash:i})}const In={activate(){window.addEventListener("popstate",ei)},inactivate(){window.removeEventListener("popstate",ei)}};var Fe=Qi,Pn=Ro,On=Dn,Ln=Yi,Mn=Xi,Gi="/",Ki="./",Vn=new RegExp(["(\\\\.)","(?:\\:(\\w+)(?:\\(((?:\\\\.|[^\\\\()])+)\\))?|\\(((?:\\\\.|[^\\\\()])+)\\))([+*?])?"].join("|"),"g");function Ro(t,e){for(var o=[],i=0,n=0,s="",r=e&&e.delimiter||Gi,l=e&&e.delimiters||Ki,a=!1,d;(d=Vn.exec(t))!==null;){var h=d[0],v=d[1],u=d.index;if(s+=t.slice(n,u),n=u+h.length,v){s+=v[1],a=!0;continue}var y="",ce=t[n],he=d[2],ne=d[3],Dt=d[4],H=d[5];if(!a&&s.length){var ee=s.length-1;l.indexOf(s[ee])>-1&&(y=s[ee],s=s.slice(0,ee))}s&&(o.push(s),s="",a=!1);var ke=y!==""&&ce!==void 0&&ce!==y,Te=H==="+"||H==="*",Ut=H==="?"||H==="*",se=y||r,nt=ne||Dt;o.push({name:he||i++,prefix:y,delimiter:se,optional:Ut,repeat:Te,partial:ke,pattern:nt?Un(nt):"[^"+ue(se)+"]+?"})}return(s||n<t.length)&&o.push(s+t.substr(n)),o}function Dn(t,e){return Yi(Ro(t,e))}function Yi(t){for(var e=new Array(t.length),o=0;o<t.length;o++)typeof t[o]=="object"&&(e[o]=new RegExp("^(?:"+t[o].pattern+")$"));return function(i,n){for(var s="",r=n&&n.encode||encodeURIComponent,l=0;l<t.length;l++){var a=t[l];if(typeof a=="string"){s+=a;continue}var d=i?i[a.name]:void 0,h;if(Array.isArray(d)){if(!a.repeat)throw new TypeError('Expected "'+a.name+'" to not repeat, but got array');if(d.length===0){if(a.optional)continue;throw new TypeError('Expected "'+a.name+'" to not be empty')}for(var v=0;v<d.length;v++){if(h=r(d[v],a),!e[l].test(h))throw new TypeError('Expected all "'+a.name+'" to match "'+a.pattern+'"');s+=(v===0?a.prefix:a.delimiter)+h}continue}if(typeof d=="string"||typeof d=="number"||typeof d=="boolean"){if(h=r(String(d),a),!e[l].test(h))throw new TypeError('Expected "'+a.name+'" to match "'+a.pattern+'", but got "'+h+'"');s+=a.prefix+h;continue}if(a.optional){a.partial&&(s+=a.prefix);continue}throw new TypeError('Expected "'+a.name+'" to be '+(a.repeat?"an array":"a string"))}return s}}function ue(t){return t.replace(/([.+*?=^!:${}()[\]|/\\])/g,"\\$1")}function Un(t){return t.replace(/([=!:$/()])/g,"\\$1")}function Ji(t){return t&&t.sensitive?"":"i"}function zn(t,e){if(!e)return t;var o=t.source.match(/\((?!\?)/g);if(o)for(var i=0;i<o.length;i++)e.push({name:i,prefix:null,delimiter:null,optional:!1,repeat:!1,partial:!1,pattern:null});return t}function jn(t,e,o){for(var i=[],n=0;n<t.length;n++)i.push(Qi(t[n],e,o).source);return new RegExp("(?:"+i.join("|")+")",Ji(o))}function Fn(t,e,o){return Xi(Ro(t,o),e,o)}function Xi(t,e,o){o=o||{};for(var i=o.strict,n=o.start!==!1,s=o.end!==!1,r=ue(o.delimiter||Gi),l=o.delimiters||Ki,a=[].concat(o.endsWith||[]).map(ue).concat("$").join("|"),d=n?"^":"",h=t.length===0,v=0;v<t.length;v++){var u=t[v];if(typeof u=="string")d+=ue(u),h=v===t.length-1&&l.indexOf(u[u.length-1])>-1;else{var y=u.repeat?"(?:"+u.pattern+")(?:"+ue(u.delimiter)+"(?:"+u.pattern+"))*":u.pattern;e&&e.push(u),u.optional?u.partial?d+=ue(u.prefix)+"("+y+")?":d+="(?:"+ue(u.prefix)+"("+y+"))?":d+=ue(u.prefix)+"("+y+")"}}return s?(i||(d+="(?:"+r+")?"),d+=a==="$"?"$":"(?="+a+")"):(i||(d+="(?:"+r+"(?="+a+"))?"),h||(d+="(?="+r+"|"+a+")")),new RegExp(d,Ji(o))}function Qi(t,e,o){return t instanceof RegExp?zn(t,e):Array.isArray(t)?jn(t,e,o):Fn(t,e,o)}Fe.parse=Pn;Fe.compile=On;Fe.tokensToFunction=Ln;Fe.tokensToRegExp=Mn;const{hasOwnProperty:Bn}=Object.prototype,_o=new Map;_o.set("|false",{keys:[],pattern:/(?:)/});function ti(t){try{return decodeURIComponent(t)}catch{return t}}function Hn(t,e,o,i,n){o=!!o;const s=`${t}|${o}`;let r=_o.get(s);if(!r){const d=[];r={keys:d,pattern:Fe(t,d,{end:o,strict:t===""})},_o.set(s,r)}const l=r.pattern.exec(e);if(!l)return null;const a=Object.assign({},n);for(let d=1;d<l.length;d++){const h=r.keys[d-1],v=h.name,u=l[d];(u!==void 0||!Bn.call(a,v))&&(h.repeat?a[v]=u?u.split(h.delimiter).map(ti):[]:a[v]=u&&ti(u))}return{path:l[0],keys:(i||[]).concat(r.keys),params:a}}function Zi(t,e,o,i,n){let s,r,l=0,a=t.path||"";return a.charAt(0)==="/"&&(o&&(a=a.substr(1)),o=!0),{next(d){if(t===d)return{done:!0};const h=t.__children=t.__children||t.children;if(!s&&(s=Hn(a,e,!h,i,n),s))return{done:!1,value:{route:t,keys:s.keys,params:s.params,path:s.path}};if(s&&h)for(;l<h.length;){if(!r){const u=h[l];u.parent=t;let y=s.path.length;y>0&&e.charAt(y)==="/"&&(y+=1),r=Zi(u,e.substr(y),o,s.keys,s.params)}const v=r.next(d);if(!v.done)return{done:!1,value:v.value};r=null,l++}return{done:!0}}}}function qn(t){if(Ee(t.route.action))return t.route.action(t)}function Wn(t,e){let o=e;for(;o;)if(o=o.parent,o===t)return!0;return!1}function Gn(t){let e=`Path '${t.pathname}' is not properly resolved due to an error.`;const o=(t.route||{}).path;return o&&(e+=` Resolution had failed on route: '${o}'`),e}function Kn(t,e){const{route:o,path:i}=e;if(o&&!o.__synthetic){const n={path:i,route:o};if(!t.chain)t.chain=[];else if(o.parent){let s=t.chain.length;for(;s--&&t.chain[s].route&&t.chain[s].route!==o.parent;)t.chain.pop()}t.chain.push(n)}}class Ge{constructor(e,o={}){if(Object(e)!==e)throw new TypeError("Invalid routes");this.baseUrl=o.baseUrl||"",this.errorHandler=o.errorHandler,this.resolveRoute=o.resolveRoute||qn,this.context=Object.assign({resolver:this},o.context),this.root=Array.isArray(e)?{path:"",__children:e,parent:null,__synthetic:!0}:e,this.root.parent=null}getRoutes(){return[...this.root.__children]}setRoutes(e){Xo(e);const o=[...bt(e)];this.root.__children=o}addRoutes(e){return Xo(e),this.root.__children.push(...bt(e)),this.getRoutes()}removeRoutes(){this.setRoutes([])}resolve(e){const o=Object.assign({},this.context,J(e)?{pathname:e}:e),i=Zi(this.root,this.__normalizePathname(o.pathname),this.baseUrl),n=this.resolveRoute;let s=null,r=null,l=o;function a(d,h=s.value.route,v){const u=v===null&&s.value.route;return s=r||i.next(u),r=null,!d&&(s.done||!Wn(h,s.value.route))?(r=s,Promise.resolve(Pe)):s.done?Promise.reject(Wi(o)):(l=Object.assign(l?{chain:l.chain?l.chain.slice(0):[]}:{},o,s.value),Kn(l,s.value),Promise.resolve(n(l)).then(y=>y!=null&&y!==Pe?(l.result=y.result||y,l):a(d,h,y)))}return o.next=a,Promise.resolve().then(()=>a(!0,this.root)).catch(d=>{const h=Gn(l);if(d?console.warn(h):d=new Error(h),d.context=d.context||l,d instanceof DOMException||(d.code=d.code||500),this.errorHandler)return l.result=this.errorHandler(d),l;throw d})}static __createUrl(e,o){return new URL(e,o)}get __effectiveBaseUrl(){return this.baseUrl?this.constructor.__createUrl(this.baseUrl,document.baseURI||document.URL).href.replace(/[^\/]*$/,""):""}__normalizePathname(e){if(!this.baseUrl)return e;const o=this.__effectiveBaseUrl,i=this.constructor.__createUrl(e,o).href;if(i.slice(0,o.length)===o)return i.slice(o.length)}}Ge.pathToRegexp=Fe;const{pathToRegexp:oi}=Ge,ii=new Map;function en(t,e,o){const i=e.name||e.component;if(i&&(t.has(i)?t.get(i).push(e):t.set(i,[e])),Array.isArray(o))for(let n=0;n<o.length;n++){const s=o[n];s.parent=e,en(t,s,s.__children||s.children)}}function ni(t,e){const o=t.get(e);if(o&&o.length>1)throw new Error(`Duplicate route with name "${e}". Try seting unique 'name' route properties.`);return o&&o[0]}function si(t){let e=t.path;return e=Array.isArray(e)?e[0]:e,e!==void 0?e:""}function Yn(t,e={}){if(!(t instanceof Ge))throw new TypeError("An instance of Resolver is expected");const o=new Map;return(i,n)=>{let s=ni(o,i);if(!s&&(o.clear(),en(o,t.root,t.root.__children),s=ni(o,i),!s))throw new Error(`Route "${i}" not found`);let r=ii.get(s.fullPath);if(!r){let a=si(s),d=s.parent;for(;d;){const y=si(d);y&&(a=y.replace(/\/$/,"")+"/"+a.replace(/^\//,"")),d=d.parent}const h=oi.parse(a),v=oi.tokensToFunction(h),u=Object.create(null);for(let y=0;y<h.length;y++)J(h[y])||(u[h[y].name]=!0);r={toPath:v,keys:u},ii.set(a,r),s.fullPath=a}let l=r.toPath(n,e)||"/";if(e.stringifyQueryParams&&n){const a={},d=Object.keys(n);for(let v=0;v<d.length;v++){const u=d[v];r.keys[u]||(a[u]=n[u])}const h=e.stringifyQueryParams(a);h&&(l+=h.charAt(0)==="?"?h:`?${h}`)}return l}}let ri=[];function Jn(t){ri.forEach(e=>e.inactivate()),t.forEach(e=>e.activate()),ri=t}const Xn=t=>{const e=getComputedStyle(t).getPropertyValue("animation-name");return e&&e!=="none"},Qn=(t,e)=>{const o=()=>{t.removeEventListener("animationend",o),e()};t.addEventListener("animationend",o)};function ai(t,e){return t.classList.add(e),new Promise(o=>{if(Xn(t)){const i=t.getBoundingClientRect(),n=`height: ${i.bottom-i.top}px; width: ${i.right-i.left}px`;t.setAttribute("style",`position: absolute; ${n}`),Qn(t,()=>{t.classList.remove(e),t.removeAttribute("style"),o()})}else t.classList.remove(e),o()})}const Zn=256;function Bt(t){return t!=null}function es(t){const e=Object.assign({},t);return delete e.next,e}function G({pathname:t="",search:e="",hash:o="",chain:i=[],params:n={},redirectFrom:s,resolver:r},l){const a=i.map(d=>d.route);return{baseUrl:r&&r.baseUrl||"",pathname:t,search:e,hash:o,routes:a,route:l||a.length&&a[a.length-1]||null,params:n,redirectFrom:s,getUrl:(d={})=>vt(pe.pathToRegexp.compile(tn(a))(Object.assign({},n,d)),r)}}function li(t,e){const o=Object.assign({},t.params);return{redirect:{pathname:e,from:t.pathname,params:o}}}function ts(t,e){e.location=G(t);const o=t.chain.map(i=>i.route).indexOf(t.route);return t.chain[o].element=e,e}function mt(t,e,o){if(Ee(t))return t.apply(o,e)}function di(t,e,o){return i=>{if(i&&(i.cancel||i.redirect))return i;if(o)return mt(o[t],e,o)}}function os(t,e){if(!Array.isArray(t)&&!St(t))throw new Error(Q(`Incorrect "children" value for the route ${e.path}: expected array or object, but got ${t}`));e.__children=[];const o=bt(t);for(let i=0;i<o.length;i++)qi(o[i]),e.__children.push(o[i])}function dt(t){if(t&&t.length){const e=t[0].parentNode;for(let o=0;o<t.length;o++)e.removeChild(t[o])}}function vt(t,e){const o=e.__effectiveBaseUrl;return o?e.constructor.__createUrl(t.replace(/^\//,""),o).pathname:t}function tn(t){return t.map(e=>e.path).reduce((e,o)=>o.length?e.replace(/\/$/,"")+"/"+o.replace(/^\//,""):e,"")}class pe extends Ge{constructor(e,o){const i=document.head.querySelector("base"),n=i&&i.getAttribute("href");super([],Object.assign({baseUrl:n&&Ge.__createUrl(n,document.URL).pathname.replace(/[^\/]*$/,"")},o)),this.resolveRoute=r=>this.__resolveRoute(r);const s=pe.NavigationTrigger;pe.setTriggers.apply(pe,Object.keys(s).map(r=>s[r])),this.baseUrl,this.ready,this.ready=Promise.resolve(e),this.location,this.location=G({resolver:this}),this.__lastStartedRenderId=0,this.__navigationEventHandler=this.__onNavigationEvent.bind(this),this.setOutlet(e),this.subscribe(),this.__createdByRouter=new WeakMap,this.__addedByRouter=new WeakMap}__resolveRoute(e){const o=e.route;let i=Promise.resolve();Ee(o.children)&&(i=i.then(()=>o.children(es(e))).then(s=>{!Bt(s)&&!Ee(o.children)&&(s=o.children),os(s,o)}));const n={redirect:s=>li(e,s),component:s=>{const r=document.createElement(s);return this.__createdByRouter.set(r,!0),r}};return i.then(()=>{if(this.__isLatestRender(e))return mt(o.action,[e,n],o)}).then(s=>{if(Bt(s)&&(s instanceof HTMLElement||s.redirect||s===Pe))return s;if(J(o.redirect))return n.redirect(o.redirect);if(o.bundle)return $n(o.bundle).then(()=>{},()=>{throw new Error(Q(`Bundle not found: ${o.bundle}. Check if the file name is correct`))})}).then(s=>{if(Bt(s))return s;if(J(o.component))return n.component(o.component)})}setOutlet(e){e&&this.__ensureOutlet(e),this.__outlet=e}getOutlet(){return this.__outlet}setRoutes(e,o=!1){return this.__previousContext=void 0,this.__urlForName=void 0,super.setRoutes(e),o||this.__onNavigationEvent(),this.ready}render(e,o){const i=++this.__lastStartedRenderId,n=Object.assign({search:"",hash:""},J(e)?{pathname:e}:e,{__renderId:i});return this.ready=this.resolve(n).then(s=>this.__fullyResolveChain(s)).then(s=>{if(this.__isLatestRender(s)){const r=this.__previousContext;if(s===r)return this.__updateBrowserHistory(r,!0),this.location;if(this.location=G(s),o&&this.__updateBrowserHistory(s,i===1),qe("location-changed",{router:this,location:this.location}),s.__skipAttach)return this.__copyUnchangedElements(s,r),this.__previousContext=s,this.location;this.__addAppearingContent(s,r);const l=this.__animateIfNeeded(s);return this.__runOnAfterEnterCallbacks(s),this.__runOnAfterLeaveCallbacks(s,r),l.then(()=>{if(this.__isLatestRender(s))return this.__removeDisappearingContent(),this.__previousContext=s,this.location})}}).catch(s=>{if(i===this.__lastStartedRenderId)throw o&&this.__updateBrowserHistory(n),dt(this.__outlet&&this.__outlet.children),this.location=G(Object.assign(n,{resolver:this})),qe("error",Object.assign({router:this,error:s},n)),s}),this.ready}__fullyResolveChain(e,o=e){return this.__findComponentContextAfterAllRedirects(o).then(i=>{const s=i!==o?i:e,l=vt(tn(i.chain),i.resolver)===i.pathname,a=(d,h=d.route,v)=>d.next(void 0,h,v).then(u=>u===null||u===Pe?l?d:h.parent!==null?a(d,h.parent,u):u:u);return a(i).then(d=>{if(d===null||d===Pe)throw Wi(s);return d&&d!==Pe&&d!==i?this.__fullyResolveChain(s,d):this.__amendWithOnBeforeCallbacks(i)})})}__findComponentContextAfterAllRedirects(e){const o=e.result;return o instanceof HTMLElement?(ts(e,o),Promise.resolve(e)):o.redirect?this.__redirect(o.redirect,e.__redirectCount,e.__renderId).then(i=>this.__findComponentContextAfterAllRedirects(i)):o instanceof Error?Promise.reject(o):Promise.reject(new Error(Q(`Invalid route resolution result for path "${e.pathname}". Expected redirect object or HTML element, but got: "${Tn(o)}". Double check the action return value for the route.`)))}__amendWithOnBeforeCallbacks(e){return this.__runOnBeforeCallbacks(e).then(o=>o===this.__previousContext||o===e?o:this.__fullyResolveChain(o))}__runOnBeforeCallbacks(e){const o=this.__previousContext||{},i=o.chain||[],n=e.chain;let s=Promise.resolve();const r=()=>({cancel:!0}),l=a=>li(e,a);if(e.__divergedChainIndex=0,e.__skipAttach=!1,i.length){for(let a=0;a<Math.min(i.length,n.length)&&!(i[a].route!==n[a].route||i[a].path!==n[a].path&&i[a].element!==n[a].element||!this.__isReusableElement(i[a].element,n[a].element));a=++e.__divergedChainIndex);if(e.__skipAttach=n.length===i.length&&e.__divergedChainIndex==n.length&&this.__isReusableElement(e.result,o.result),e.__skipAttach){for(let a=n.length-1;a>=0;a--)s=this.__runOnBeforeLeaveCallbacks(s,e,{prevent:r},i[a]);for(let a=0;a<n.length;a++)s=this.__runOnBeforeEnterCallbacks(s,e,{prevent:r,redirect:l},n[a]),i[a].element.location=G(e,i[a].route)}else for(let a=i.length-1;a>=e.__divergedChainIndex;a--)s=this.__runOnBeforeLeaveCallbacks(s,e,{prevent:r},i[a])}if(!e.__skipAttach)for(let a=0;a<n.length;a++)a<e.__divergedChainIndex?a<i.length&&i[a].element&&(i[a].element.location=G(e,i[a].route)):(s=this.__runOnBeforeEnterCallbacks(s,e,{prevent:r,redirect:l},n[a]),n[a].element&&(n[a].element.location=G(e,n[a].route)));return s.then(a=>{if(a){if(a.cancel)return this.__previousContext.__renderId=e.__renderId,this.__previousContext;if(a.redirect)return this.__redirect(a.redirect,e.__redirectCount,e.__renderId)}return e})}__runOnBeforeLeaveCallbacks(e,o,i,n){const s=G(o);return e.then(r=>{if(this.__isLatestRender(o))return di("onBeforeLeave",[s,i,this],n.element)(r)}).then(r=>{if(!(r||{}).redirect)return r})}__runOnBeforeEnterCallbacks(e,o,i,n){const s=G(o,n.route);return e.then(r=>{if(this.__isLatestRender(o))return di("onBeforeEnter",[s,i,this],n.element)(r)})}__isReusableElement(e,o){return e&&o?this.__createdByRouter.get(e)&&this.__createdByRouter.get(o)?e.localName===o.localName:e===o:!1}__isLatestRender(e){return e.__renderId===this.__lastStartedRenderId}__redirect(e,o,i){if(o>Zn)throw new Error(Q(`Too many redirects when rendering ${e.from}`));return this.resolve({pathname:this.urlForPath(e.pathname,e.params),redirectFrom:e.from,__redirectCount:(o||0)+1,__renderId:i})}__ensureOutlet(e=this.__outlet){if(!(e instanceof Node))throw new TypeError(Q(`Expected router outlet to be a valid DOM Node (but got ${e})`))}__updateBrowserHistory({pathname:e,search:o="",hash:i=""},n){if(window.location.pathname!==e||window.location.search!==o||window.location.hash!==i){const s=n?"replaceState":"pushState";window.history[s](null,document.title,e+o+i),window.dispatchEvent(new PopStateEvent("popstate",{state:"vaadin-router-ignore"}))}}__copyUnchangedElements(e,o){let i=this.__outlet;for(let n=0;n<e.__divergedChainIndex;n++){const s=o&&o.chain[n].element;if(s)if(s.parentNode===i)e.chain[n].element=s,i=s;else break}return i}__addAppearingContent(e,o){this.__ensureOutlet(),this.__removeAppearingContent();const i=this.__copyUnchangedElements(e,o);this.__appearingContent=[],this.__disappearingContent=Array.from(i.children).filter(s=>this.__addedByRouter.get(s)&&s!==e.result);let n=i;for(let s=e.__divergedChainIndex;s<e.chain.length;s++){const r=e.chain[s].element;r&&(n.appendChild(r),this.__addedByRouter.set(r,!0),n===i&&this.__appearingContent.push(r),n=r)}}__removeDisappearingContent(){this.__disappearingContent&&dt(this.__disappearingContent),this.__disappearingContent=null,this.__appearingContent=null}__removeAppearingContent(){this.__disappearingContent&&this.__appearingContent&&(dt(this.__appearingContent),this.__disappearingContent=null,this.__appearingContent=null)}__runOnAfterLeaveCallbacks(e,o){if(o)for(let i=o.chain.length-1;i>=e.__divergedChainIndex&&this.__isLatestRender(e);i--){const n=o.chain[i].element;if(n)try{const s=G(e);mt(n.onAfterLeave,[s,{},o.resolver],n)}finally{this.__disappearingContent.indexOf(n)>-1&&dt(n.children)}}}__runOnAfterEnterCallbacks(e){for(let o=e.__divergedChainIndex;o<e.chain.length&&this.__isLatestRender(e);o++){const i=e.chain[o].element||{},n=G(e,e.chain[o].route);mt(i.onAfterEnter,[n,{},e.resolver],i)}}__animateIfNeeded(e){const o=(this.__disappearingContent||[])[0],i=(this.__appearingContent||[])[0],n=[],s=e.chain;let r;for(let l=s.length;l>0;l--)if(s[l-1].route.animate){r=s[l-1].route.animate;break}if(o&&i&&r){const l=St(r)&&r.leave||"leaving",a=St(r)&&r.enter||"entering";n.push(ai(o,l)),n.push(ai(i,a))}return Promise.all(n).then(()=>e)}subscribe(){window.addEventListener("vaadin-router-go",this.__navigationEventHandler)}unsubscribe(){window.removeEventListener("vaadin-router-go",this.__navigationEventHandler)}__onNavigationEvent(e){const{pathname:o,search:i,hash:n}=e?e.detail:window.location;J(this.__normalizePathname(o))&&(e&&e.preventDefault&&e.preventDefault(),this.render({pathname:o,search:i,hash:n},!0))}static setTriggers(...e){Jn(e)}urlForName(e,o){return this.__urlForName||(this.__urlForName=Yn(this)),vt(this.__urlForName(e,o),this)}urlForPath(e,o){return vt(pe.pathToRegexp.compile(e)(o),this)}static go(e){const{pathname:o,search:i,hash:n}=J(e)?this.__createUrl(e,"http://a"):e;return qe("go",{pathname:o,search:i,hash:n})}}const is=/\/\*[\*!]\s+vaadin-dev-mode:start([\s\S]*)vaadin-dev-mode:end\s+\*\*\//i,ft=window.Vaadin&&window.Vaadin.Flow&&window.Vaadin.Flow.clients;function ns(){function t(){return!0}return on(t)}function ss(){try{return rs()?!0:as()?ft?!ls():!ns():!1}catch{return!1}}function rs(){return localStorage.getItem("vaadin.developmentmode.force")}function as(){return["localhost","127.0.0.1"].indexOf(window.location.hostname)>=0}function ls(){return!!(ft&&Object.keys(ft).map(e=>ft[e]).filter(e=>e.productionMode).length>0)}function on(t,e){if(typeof t!="function")return;const o=is.exec(t.toString());if(o)try{t=new Function(o[1])}catch(i){console.log("vaadin-development-mode-detector: uncommentAndRun() failed",i)}return t(e)}window.Vaadin=window.Vaadin||{};const ci=function(t,e){if(window.Vaadin.developmentMode)return on(t,e)};window.Vaadin.developmentMode===void 0&&(window.Vaadin.developmentMode=ss());function ds(){}const cs=function(){if(typeof ci=="function")return ci(ds)};window.Vaadin=window.Vaadin||{};window.Vaadin.registrations=window.Vaadin.registrations||[];window.Vaadin.registrations.push({is:"@vaadin/router",version:"1.7.4"});cs();pe.NavigationTrigger={POPSTATE:In,CLICK:Rn};var Ht,R;(function(t){t.CONNECTED="connected",t.LOADING="loading",t.RECONNECTING="reconnecting",t.CONNECTION_LOST="connection-lost"})(R||(R={}));class hs{constructor(e){this.stateChangeListeners=new Set,this.loadingCount=0,this.connectionState=e,this.serviceWorkerMessageListener=this.serviceWorkerMessageListener.bind(this),navigator.serviceWorker&&(navigator.serviceWorker.addEventListener("message",this.serviceWorkerMessageListener),navigator.serviceWorker.ready.then(o=>{var i;(i=o==null?void 0:o.active)===null||i===void 0||i.postMessage({method:"Vaadin.ServiceWorker.isConnectionLost",id:"Vaadin.ServiceWorker.isConnectionLost"})}))}addStateChangeListener(e){this.stateChangeListeners.add(e)}removeStateChangeListener(e){this.stateChangeListeners.delete(e)}loadingStarted(){this.state=R.LOADING,this.loadingCount+=1}loadingFinished(){this.decreaseLoadingCount(R.CONNECTED)}loadingFailed(){this.decreaseLoadingCount(R.CONNECTION_LOST)}decreaseLoadingCount(e){this.loadingCount>0&&(this.loadingCount-=1,this.loadingCount===0&&(this.state=e))}get state(){return this.connectionState}set state(e){if(e!==this.connectionState){const o=this.connectionState;this.connectionState=e,this.loadingCount=0;for(const i of this.stateChangeListeners)i(o,this.connectionState)}}get online(){return this.connectionState===R.CONNECTED||this.connectionState===R.LOADING}get offline(){return!this.online}serviceWorkerMessageListener(e){typeof e.data=="object"&&e.data.id==="Vaadin.ServiceWorker.isConnectionLost"&&(e.data.result===!0&&(this.state=R.CONNECTION_LOST),navigator.serviceWorker.removeEventListener("message",this.serviceWorkerMessageListener))}}const us=t=>!!(t==="localhost"||t==="[::1]"||t.match(/^127\.\d+\.\d+\.\d+$/)),ct=window;if(!(!((Ht=ct.Vaadin)===null||Ht===void 0)&&Ht.connectionState)){let t;us(window.location.hostname)?t=!0:t=navigator.onLine,ct.Vaadin=ct.Vaadin||{},ct.Vaadin.connectionState=new hs(t?R.CONNECTED:R.CONNECTION_LOST)}function B(t,e,o,i){var n=arguments.length,s=n<3?e:i===null?i=Object.getOwnPropertyDescriptor(e,o):i,r;if(typeof Reflect=="object"&&typeof Reflect.decorate=="function")s=Reflect.decorate(t,e,o,i);else for(var l=t.length-1;l>=0;l--)(r=t[l])&&(s=(n<3?r(s):n>3?r(e,o,s):r(e,o))||s);return n>3&&s&&Object.defineProperty(e,o,s),s}/**
 * @license
 * Copyright 2019 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const ps=!1,gt=window,No=gt.ShadowRoot&&(gt.ShadyCSS===void 0||gt.ShadyCSS.nativeShadow)&&"adoptedStyleSheets"in Document.prototype&&"replace"in CSSStyleSheet.prototype,Io=Symbol(),hi=new WeakMap;class nn{constructor(e,o,i){if(this._$cssResult$=!0,i!==Io)throw new Error("CSSResult is not constructable. Use `unsafeCSS` or `css` instead.");this.cssText=e,this._strings=o}get styleSheet(){let e=this._styleSheet;const o=this._strings;if(No&&e===void 0){const i=o!==void 0&&o.length===1;i&&(e=hi.get(o)),e===void 0&&((this._styleSheet=e=new CSSStyleSheet).replaceSync(this.cssText),i&&hi.set(o,e))}return e}toString(){return this.cssText}}const ms=t=>{if(t._$cssResult$===!0)return t.cssText;if(typeof t=="number")return t;throw new Error(`Value passed to 'css' function must be a 'css' function result: ${t}. Use 'unsafeCSS' to pass non-literal values, but take care to ensure page security.`)},vs=t=>new nn(typeof t=="string"?t:String(t),void 0,Io),$=(t,...e)=>{const o=t.length===1?t[0]:e.reduce((i,n,s)=>i+ms(n)+t[s+1],t[0]);return new nn(o,t,Io)},fs=(t,e)=>{No?t.adoptedStyleSheets=e.map(o=>o instanceof CSSStyleSheet?o:o.styleSheet):e.forEach(o=>{const i=document.createElement("style"),n=gt.litNonce;n!==void 0&&i.setAttribute("nonce",n),i.textContent=o.cssText,t.appendChild(i)})},gs=t=>{let e="";for(const o of t.cssRules)e+=o.cssText;return vs(e)},ui=No||ps?t=>t:t=>t instanceof CSSStyleSheet?gs(t):t;/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var qt,Wt,Gt,sn;const oe=window;let rn,me;const pi=oe.trustedTypes,ys=pi?pi.emptyScript:"",yt=oe.reactiveElementPolyfillSupportDevMode;{const t=(qt=oe.litIssuedWarnings)!==null&&qt!==void 0?qt:oe.litIssuedWarnings=new Set;me=(e,o)=>{o+=` See https://lit.dev/msg/${e} for more information.`,t.has(o)||(console.warn(o),t.add(o))},me("dev-mode","Lit is in dev mode. Not recommended for production!"),!((Wt=oe.ShadyDOM)===null||Wt===void 0)&&Wt.inUse&&yt===void 0&&me("polyfill-support-missing","Shadow DOM is being polyfilled via `ShadyDOM` but the `polyfill-support` module has not been loaded."),rn=e=>({then:(o,i)=>{me("request-update-promise",`The \`requestUpdate\` method should no longer return a Promise but does so on \`${e}\`. Use \`updateComplete\` instead.`),o!==void 0&&o(!1)}})}const Kt=t=>{oe.emitLitDebugLogEvents&&oe.dispatchEvent(new CustomEvent("lit-debug",{detail:t}))},an=(t,e)=>t,bo={toAttribute(t,e){switch(e){case Boolean:t=t?ys:null;break;case Object:case Array:t=t==null?t:JSON.stringify(t);break}return t},fromAttribute(t,e){let o=t;switch(e){case Boolean:o=t!==null;break;case Number:o=t===null?null:Number(t);break;case Object:case Array:try{o=JSON.parse(t)}catch{o=null}break}return o}},ln=(t,e)=>e!==t&&(e===e||t===t),Yt={attribute:!0,type:String,converter:bo,reflect:!1,hasChanged:ln},wo="finalized";class ie extends HTMLElement{constructor(){super(),this.__instanceProperties=new Map,this.isUpdatePending=!1,this.hasUpdated=!1,this.__reflectingProperty=null,this.__initialize()}static addInitializer(e){var o;this.finalize(),((o=this._initializers)!==null&&o!==void 0?o:this._initializers=[]).push(e)}static get observedAttributes(){this.finalize();const e=[];return this.elementProperties.forEach((o,i)=>{const n=this.__attributeNameForProperty(i,o);n!==void 0&&(this.__attributeToPropertyMap.set(n,i),e.push(n))}),e}static createProperty(e,o=Yt){var i;if(o.state&&(o.attribute=!1),this.finalize(),this.elementProperties.set(e,o),!o.noAccessor&&!this.prototype.hasOwnProperty(e)){const n=typeof e=="symbol"?Symbol():`__${e}`,s=this.getPropertyDescriptor(e,n,o);s!==void 0&&(Object.defineProperty(this.prototype,e,s),this.hasOwnProperty("__reactivePropertyKeys")||(this.__reactivePropertyKeys=new Set((i=this.__reactivePropertyKeys)!==null&&i!==void 0?i:[])),this.__reactivePropertyKeys.add(e))}}static getPropertyDescriptor(e,o,i){return{get(){return this[o]},set(n){const s=this[e];this[o]=n,this.requestUpdate(e,s,i)},configurable:!0,enumerable:!0}}static getPropertyOptions(e){return this.elementProperties.get(e)||Yt}static finalize(){if(this.hasOwnProperty(wo))return!1;this[wo]=!0;const e=Object.getPrototypeOf(this);if(e.finalize(),e._initializers!==void 0&&(this._initializers=[...e._initializers]),this.elementProperties=new Map(e.elementProperties),this.__attributeToPropertyMap=new Map,this.hasOwnProperty(an("properties"))){const o=this.properties,i=[...Object.getOwnPropertyNames(o),...Object.getOwnPropertySymbols(o)];for(const n of i)this.createProperty(n,o[n])}this.elementStyles=this.finalizeStyles(this.styles);{const o=(i,n=!1)=>{this.prototype.hasOwnProperty(i)&&me(n?"renamed-api":"removed-api",`\`${i}\` is implemented on class ${this.name}. It has been ${n?"renamed":"removed"} in this version of LitElement.`)};o("initialize"),o("requestUpdateInternal"),o("_getUpdateComplete",!0)}return!0}static finalizeStyles(e){const o=[];if(Array.isArray(e)){const i=new Set(e.flat(1/0).reverse());for(const n of i)o.unshift(ui(n))}else e!==void 0&&o.push(ui(e));return o}static __attributeNameForProperty(e,o){const i=o.attribute;return i===!1?void 0:typeof i=="string"?i:typeof e=="string"?e.toLowerCase():void 0}__initialize(){var e;this.__updatePromise=new Promise(o=>this.enableUpdating=o),this._$changedProperties=new Map,this.__saveInstanceProperties(),this.requestUpdate(),(e=this.constructor._initializers)===null||e===void 0||e.forEach(o=>o(this))}addController(e){var o,i;((o=this.__controllers)!==null&&o!==void 0?o:this.__controllers=[]).push(e),this.renderRoot!==void 0&&this.isConnected&&((i=e.hostConnected)===null||i===void 0||i.call(e))}removeController(e){var o;(o=this.__controllers)===null||o===void 0||o.splice(this.__controllers.indexOf(e)>>>0,1)}__saveInstanceProperties(){this.constructor.elementProperties.forEach((e,o)=>{this.hasOwnProperty(o)&&(this.__instanceProperties.set(o,this[o]),delete this[o])})}createRenderRoot(){var e;const o=(e=this.shadowRoot)!==null&&e!==void 0?e:this.attachShadow(this.constructor.shadowRootOptions);return fs(o,this.constructor.elementStyles),o}connectedCallback(){var e;this.renderRoot===void 0&&(this.renderRoot=this.createRenderRoot()),this.enableUpdating(!0),(e=this.__controllers)===null||e===void 0||e.forEach(o=>{var i;return(i=o.hostConnected)===null||i===void 0?void 0:i.call(o)})}enableUpdating(e){}disconnectedCallback(){var e;(e=this.__controllers)===null||e===void 0||e.forEach(o=>{var i;return(i=o.hostDisconnected)===null||i===void 0?void 0:i.call(o)})}attributeChangedCallback(e,o,i){this._$attributeToProperty(e,i)}__propertyToAttribute(e,o,i=Yt){var n;const s=this.constructor.__attributeNameForProperty(e,i);if(s!==void 0&&i.reflect===!0){const l=(((n=i.converter)===null||n===void 0?void 0:n.toAttribute)!==void 0?i.converter:bo).toAttribute(o,i.type);this.constructor.enabledWarnings.indexOf("migration")>=0&&l===void 0&&me("undefined-attribute-value",`The attribute value for the ${e} property is undefined on element ${this.localName}. The attribute will be removed, but in the previous version of \`ReactiveElement\`, the attribute would not have changed.`),this.__reflectingProperty=e,l==null?this.removeAttribute(s):this.setAttribute(s,l),this.__reflectingProperty=null}}_$attributeToProperty(e,o){var i;const n=this.constructor,s=n.__attributeToPropertyMap.get(e);if(s!==void 0&&this.__reflectingProperty!==s){const r=n.getPropertyOptions(s),l=typeof r.converter=="function"?{fromAttribute:r.converter}:((i=r.converter)===null||i===void 0?void 0:i.fromAttribute)!==void 0?r.converter:bo;this.__reflectingProperty=s,this[s]=l.fromAttribute(o,r.type),this.__reflectingProperty=null}}requestUpdate(e,o,i){let n=!0;return e!==void 0&&(i=i||this.constructor.getPropertyOptions(e),(i.hasChanged||ln)(this[e],o)?(this._$changedProperties.has(e)||this._$changedProperties.set(e,o),i.reflect===!0&&this.__reflectingProperty!==e&&(this.__reflectingProperties===void 0&&(this.__reflectingProperties=new Map),this.__reflectingProperties.set(e,i))):n=!1),!this.isUpdatePending&&n&&(this.__updatePromise=this.__enqueueUpdate()),rn(this.localName)}async __enqueueUpdate(){this.isUpdatePending=!0;try{await this.__updatePromise}catch(o){Promise.reject(o)}const e=this.scheduleUpdate();return e!=null&&await e,!this.isUpdatePending}scheduleUpdate(){return this.performUpdate()}performUpdate(){var e,o;if(!this.isUpdatePending)return;if(Kt==null||Kt({kind:"update"}),!this.hasUpdated){const s=[];if((e=this.constructor.__reactivePropertyKeys)===null||e===void 0||e.forEach(r=>{var l;this.hasOwnProperty(r)&&!(!((l=this.__instanceProperties)===null||l===void 0)&&l.has(r))&&s.push(r)}),s.length)throw new Error(`The following properties on element ${this.localName} will not trigger updates as expected because they are set using class fields: ${s.join(", ")}. Native class fields and some compiled output will overwrite accessors used for detecting changes. See https://lit.dev/msg/class-field-shadowing for more information.`)}this.__instanceProperties&&(this.__instanceProperties.forEach((s,r)=>this[r]=s),this.__instanceProperties=void 0);let i=!1;const n=this._$changedProperties;try{i=this.shouldUpdate(n),i?(this.willUpdate(n),(o=this.__controllers)===null||o===void 0||o.forEach(s=>{var r;return(r=s.hostUpdate)===null||r===void 0?void 0:r.call(s)}),this.update(n)):this.__markUpdated()}catch(s){throw i=!1,this.__markUpdated(),s}i&&this._$didUpdate(n)}willUpdate(e){}_$didUpdate(e){var o;(o=this.__controllers)===null||o===void 0||o.forEach(i=>{var n;return(n=i.hostUpdated)===null||n===void 0?void 0:n.call(i)}),this.hasUpdated||(this.hasUpdated=!0,this.firstUpdated(e)),this.updated(e),this.isUpdatePending&&this.constructor.enabledWarnings.indexOf("change-in-update")>=0&&me("change-in-update",`Element ${this.localName} scheduled an update (generally because a property was set) after an update completed, causing a new update to be scheduled. This is inefficient and should be avoided unless the next update can only be scheduled as a side effect of the previous update.`)}__markUpdated(){this._$changedProperties=new Map,this.isUpdatePending=!1}get updateComplete(){return this.getUpdateComplete()}getUpdateComplete(){return this.__updatePromise}shouldUpdate(e){return!0}update(e){this.__reflectingProperties!==void 0&&(this.__reflectingProperties.forEach((o,i)=>this.__propertyToAttribute(i,this[i],o)),this.__reflectingProperties=void 0),this.__markUpdated()}updated(e){}firstUpdated(e){}}sn=wo;ie[sn]=!0;ie.elementProperties=new Map;ie.elementStyles=[];ie.shadowRootOptions={mode:"open"};yt==null||yt({ReactiveElement:ie});{ie.enabledWarnings=["change-in-update"];const t=function(e){e.hasOwnProperty(an("enabledWarnings"))||(e.enabledWarnings=e.enabledWarnings.slice())};ie.enableWarning=function(e){t(this),this.enabledWarnings.indexOf(e)<0&&this.enabledWarnings.push(e)},ie.disableWarning=function(e){t(this);const o=this.enabledWarnings.indexOf(e);o>=0&&this.enabledWarnings.splice(o,1)}}((Gt=oe.reactiveElementVersions)!==null&&Gt!==void 0?Gt:oe.reactiveElementVersions=[]).push("1.6.3");oe.reactiveElementVersions.length>1&&me("multiple-versions","Multiple versions of Lit loaded. Loading multiple versions is not recommended.");/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var Jt,Xt,Qt,Zt;const F=window,b=t=>{F.emitLitDebugLogEvents&&F.dispatchEvent(new CustomEvent("lit-debug",{detail:t}))};let _s=0,xt;(Jt=F.litIssuedWarnings)!==null&&Jt!==void 0||(F.litIssuedWarnings=new Set),xt=(t,e)=>{e+=t?` See https://lit.dev/msg/${t} for more information.`:"",F.litIssuedWarnings.has(e)||(console.warn(e),F.litIssuedWarnings.add(e))},xt("dev-mode","Lit is in dev mode. Not recommended for production!");const q=!((Xt=F.ShadyDOM)===null||Xt===void 0)&&Xt.inUse&&((Qt=F.ShadyDOM)===null||Qt===void 0?void 0:Qt.noPatch)===!0?F.ShadyDOM.wrap:t=>t,Me=F.trustedTypes,mi=Me?Me.createPolicy("lit-html",{createHTML:t=>t}):void 0,bs=t=>t,Mt=(t,e,o)=>bs,ws=t=>{if(Ce!==Mt)throw new Error("Attempted to overwrite existing lit-html security policy. setSanitizeDOMValueFactory should be called at most once.");Ce=t},Es=()=>{Ce=Mt},Eo=(t,e,o)=>Ce(t,e,o),So="$lit$",re=`lit$${String(Math.random()).slice(9)}$`,dn="?"+re,Ss=`<${dn}>`,Se=document,Ke=()=>Se.createComment(""),Ye=t=>t===null||typeof t!="object"&&typeof t!="function",cn=Array.isArray,xs=t=>cn(t)||typeof(t==null?void 0:t[Symbol.iterator])=="function",eo=`[ 	
\f\r]`,Cs=`[^ 	
\f\r"'\`<>=]`,ks=`[^\\s"'>=/]`,Be=/<(?:(!--|\/[^a-zA-Z])|(\/?[a-zA-Z][^>\s]*)|(\/?$))/g,vi=1,to=2,Ts=3,fi=/-->/g,gi=/>/g,ge=new RegExp(`>|${eo}(?:(${ks}+)(${eo}*=${eo}*(?:${Cs}|("|')|))|$)`,"g"),$s=0,yi=1,As=2,_i=3,oo=/'/g,io=/"/g,hn=/^(?:script|style|textarea|title)$/i,Rs=1,Ct=2,Po=1,kt=2,Ns=3,Is=4,Ps=5,Oo=6,Os=7,un=t=>(e,...o)=>(e.some(i=>i===void 0)&&console.warn(`Some template strings are undefined.
This is probably caused by illegal octal escape sequences.`),{_$litType$:t,strings:e,values:o}),_=un(Rs),Ie=un(Ct),xe=Symbol.for("lit-noChange"),T=Symbol.for("lit-nothing"),bi=new WeakMap,be=Se.createTreeWalker(Se,129,null,!1);let Ce=Mt;function pn(t,e){if(!Array.isArray(t)||!t.hasOwnProperty("raw")){let o="invalid template strings array";throw o=`
          Internal Error: expected template strings to be an array
          with a 'raw' field. Faking a template strings array by
          calling html or svg like an ordinary function is effectively
          the same as calling unsafeHtml and can lead to major security
          issues, e.g. opening your code up to XSS attacks.
          If you're using the html or svg tagged template functions normally
          and still seeing this error, please file a bug at
          https://github.com/lit/lit/issues/new?template=bug_report.md
          and include information about your build tooling, if any.
        `.trim().replace(/\n */g,`
`),new Error(o)}return mi!==void 0?mi.createHTML(e):e}const Ls=(t,e)=>{const o=t.length-1,i=[];let n=e===Ct?"<svg>":"",s,r=Be;for(let a=0;a<o;a++){const d=t[a];let h=-1,v,u=0,y;for(;u<d.length&&(r.lastIndex=u,y=r.exec(d),y!==null);)if(u=r.lastIndex,r===Be){if(y[vi]==="!--")r=fi;else if(y[vi]!==void 0)r=gi;else if(y[to]!==void 0)hn.test(y[to])&&(s=new RegExp(`</${y[to]}`,"g")),r=ge;else if(y[Ts]!==void 0)throw new Error("Bindings in tag names are not supported. Please use static templates instead. See https://lit.dev/docs/templates/expressions/#static-expressions")}else r===ge?y[$s]===">"?(r=s??Be,h=-1):y[yi]===void 0?h=-2:(h=r.lastIndex-y[As].length,v=y[yi],r=y[_i]===void 0?ge:y[_i]==='"'?io:oo):r===io||r===oo?r=ge:r===fi||r===gi?r=Be:(r=ge,s=void 0);console.assert(h===-1||r===ge||r===oo||r===io,"unexpected parse state B");const ce=r===ge&&t[a+1].startsWith("/>")?" ":"";n+=r===Be?d+Ss:h>=0?(i.push(v),d.slice(0,h)+So+d.slice(h)+re+ce):d+re+(h===-2?(i.push(void 0),a):ce)}const l=n+(t[o]||"<?>")+(e===Ct?"</svg>":"");return[pn(t,l),i]};class Je{constructor({strings:e,["_$litType$"]:o},i){this.parts=[];let n,s=0,r=0;const l=e.length-1,a=this.parts,[d,h]=Ls(e,o);if(this.el=Je.createElement(d,i),be.currentNode=this.el.content,o===Ct){const v=this.el.content,u=v.firstChild;u.remove(),v.append(...u.childNodes)}for(;(n=be.nextNode())!==null&&a.length<l;){if(n.nodeType===1){{const v=n.localName;if(/^(?:textarea|template)$/i.test(v)&&n.innerHTML.includes(re)){const u=`Expressions are not supported inside \`${v}\` elements. See https://lit.dev/msg/expression-in-${v} for more information.`;if(v==="template")throw new Error(u);xt("",u)}}if(n.hasAttributes()){const v=[];for(const u of n.getAttributeNames())if(u.endsWith(So)||u.startsWith(re)){const y=h[r++];if(v.push(u),y!==void 0){const he=n.getAttribute(y.toLowerCase()+So).split(re),ne=/([.?@])?(.*)/.exec(y);a.push({type:Po,index:s,name:ne[2],strings:he,ctor:ne[1]==="."?Vs:ne[1]==="?"?Us:ne[1]==="@"?zs:Vt})}else a.push({type:Oo,index:s})}for(const u of v)n.removeAttribute(u)}if(hn.test(n.tagName)){const v=n.textContent.split(re),u=v.length-1;if(u>0){n.textContent=Me?Me.emptyScript:"";for(let y=0;y<u;y++)n.append(v[y],Ke()),be.nextNode(),a.push({type:kt,index:++s});n.append(v[u],Ke())}}}else if(n.nodeType===8)if(n.data===dn)a.push({type:kt,index:s});else{let u=-1;for(;(u=n.data.indexOf(re,u+1))!==-1;)a.push({type:Os,index:s}),u+=re.length-1}s++}b==null||b({kind:"template prep",template:this,clonableTemplate:this.el,parts:this.parts,strings:e})}static createElement(e,o){const i=Se.createElement("template");return i.innerHTML=e,i}}function Ve(t,e,o=t,i){var n,s,r,l;if(e===xe)return e;let a=i!==void 0?(n=o.__directives)===null||n===void 0?void 0:n[i]:o.__directive;const d=Ye(e)?void 0:e._$litDirective$;return(a==null?void 0:a.constructor)!==d&&((s=a==null?void 0:a._$notifyDirectiveConnectionChanged)===null||s===void 0||s.call(a,!1),d===void 0?a=void 0:(a=new d(t),a._$initialize(t,o,i)),i!==void 0?((r=(l=o).__directives)!==null&&r!==void 0?r:l.__directives=[])[i]=a:o.__directive=a),a!==void 0&&(e=Ve(t,a._$resolve(t,e.values),a,i)),e}class Ms{constructor(e,o){this._$parts=[],this._$disconnectableChildren=void 0,this._$template=e,this._$parent=o}get parentNode(){return this._$parent.parentNode}get _$isConnected(){return this._$parent._$isConnected}_clone(e){var o;const{el:{content:i},parts:n}=this._$template,s=((o=e==null?void 0:e.creationScope)!==null&&o!==void 0?o:Se).importNode(i,!0);be.currentNode=s;let r=be.nextNode(),l=0,a=0,d=n[0];for(;d!==void 0;){if(l===d.index){let h;d.type===kt?h=new ot(r,r.nextSibling,this,e):d.type===Po?h=new d.ctor(r,d.name,d.strings,this,e):d.type===Oo&&(h=new js(r,this,e)),this._$parts.push(h),d=n[++a]}l!==(d==null?void 0:d.index)&&(r=be.nextNode(),l++)}return be.currentNode=Se,s}_update(e){let o=0;for(const i of this._$parts)i!==void 0&&(b==null||b({kind:"set part",part:i,value:e[o],valueIndex:o,values:e,templateInstance:this}),i.strings!==void 0?(i._$setValue(e,i,o),o+=i.strings.length-2):i._$setValue(e[o])),o++}}class ot{constructor(e,o,i,n){var s;this.type=kt,this._$committedValue=T,this._$disconnectableChildren=void 0,this._$startNode=e,this._$endNode=o,this._$parent=i,this.options=n,this.__isConnected=(s=n==null?void 0:n.isConnected)!==null&&s!==void 0?s:!0,this._textSanitizer=void 0}get _$isConnected(){var e,o;return(o=(e=this._$parent)===null||e===void 0?void 0:e._$isConnected)!==null&&o!==void 0?o:this.__isConnected}get parentNode(){let e=q(this._$startNode).parentNode;const o=this._$parent;return o!==void 0&&(e==null?void 0:e.nodeType)===11&&(e=o.parentNode),e}get startNode(){return this._$startNode}get endNode(){return this._$endNode}_$setValue(e,o=this){var i;if(this.parentNode===null)throw new Error("This `ChildPart` has no `parentNode` and therefore cannot accept a value. This likely means the element containing the part was manipulated in an unsupported way outside of Lit's control such that the part's marker nodes were ejected from DOM. For example, setting the element's `innerHTML` or `textContent` can do this.");if(e=Ve(this,e,o),Ye(e))e===T||e==null||e===""?(this._$committedValue!==T&&(b==null||b({kind:"commit nothing to child",start:this._$startNode,end:this._$endNode,parent:this._$parent,options:this.options}),this._$clear()),this._$committedValue=T):e!==this._$committedValue&&e!==xe&&this._commitText(e);else if(e._$litType$!==void 0)this._commitTemplateResult(e);else if(e.nodeType!==void 0){if(((i=this.options)===null||i===void 0?void 0:i.host)===e){this._commitText("[probable mistake: rendered a template's host in itself (commonly caused by writing ${this} in a template]"),console.warn("Attempted to render the template host",e,"inside itself. This is almost always a mistake, and in dev mode ","we render some warning text. In production however, we'll ","render it, which will usually result in an error, and sometimes ","in the element disappearing from the DOM.");return}this._commitNode(e)}else xs(e)?this._commitIterable(e):this._commitText(e)}_insert(e){return q(q(this._$startNode).parentNode).insertBefore(e,this._$endNode)}_commitNode(e){var o;if(this._$committedValue!==e){if(this._$clear(),Ce!==Mt){const i=(o=this._$startNode.parentNode)===null||o===void 0?void 0:o.nodeName;if(i==="STYLE"||i==="SCRIPT"){let n="Forbidden";throw i==="STYLE"?n="Lit does not support binding inside style nodes. This is a security risk, as style injection attacks can exfiltrate data and spoof UIs. Consider instead using css`...` literals to compose styles, and make do dynamic styling with css custom properties, ::parts, <slot>s, and by mutating the DOM rather than stylesheets.":n="Lit does not support binding inside script nodes. This is a security risk, as it could allow arbitrary code execution.",new Error(n)}}b==null||b({kind:"commit node",start:this._$startNode,parent:this._$parent,value:e,options:this.options}),this._$committedValue=this._insert(e)}}_commitText(e){if(this._$committedValue!==T&&Ye(this._$committedValue)){const o=q(this._$startNode).nextSibling;this._textSanitizer===void 0&&(this._textSanitizer=Eo(o,"data","property")),e=this._textSanitizer(e),b==null||b({kind:"commit text",node:o,value:e,options:this.options}),o.data=e}else{const o=Se.createTextNode("");this._commitNode(o),this._textSanitizer===void 0&&(this._textSanitizer=Eo(o,"data","property")),e=this._textSanitizer(e),b==null||b({kind:"commit text",node:o,value:e,options:this.options}),o.data=e}this._$committedValue=e}_commitTemplateResult(e){var o;const{values:i,["_$litType$"]:n}=e,s=typeof n=="number"?this._$getTemplate(e):(n.el===void 0&&(n.el=Je.createElement(pn(n.h,n.h[0]),this.options)),n);if(((o=this._$committedValue)===null||o===void 0?void 0:o._$template)===s)b==null||b({kind:"template updating",template:s,instance:this._$committedValue,parts:this._$committedValue._$parts,options:this.options,values:i}),this._$committedValue._update(i);else{const r=new Ms(s,this),l=r._clone(this.options);b==null||b({kind:"template instantiated",template:s,instance:r,parts:r._$parts,options:this.options,fragment:l,values:i}),r._update(i),b==null||b({kind:"template instantiated and updated",template:s,instance:r,parts:r._$parts,options:this.options,fragment:l,values:i}),this._commitNode(l),this._$committedValue=r}}_$getTemplate(e){let o=bi.get(e.strings);return o===void 0&&bi.set(e.strings,o=new Je(e)),o}_commitIterable(e){cn(this._$committedValue)||(this._$committedValue=[],this._$clear());const o=this._$committedValue;let i=0,n;for(const s of e)i===o.length?o.push(n=new ot(this._insert(Ke()),this._insert(Ke()),this,this.options)):n=o[i],n._$setValue(s),i++;i<o.length&&(this._$clear(n&&q(n._$endNode).nextSibling,i),o.length=i)}_$clear(e=q(this._$startNode).nextSibling,o){var i;for((i=this._$notifyConnectionChanged)===null||i===void 0||i.call(this,!1,!0,o);e&&e!==this._$endNode;){const n=q(e).nextSibling;q(e).remove(),e=n}}setConnected(e){var o;if(this._$parent===void 0)this.__isConnected=e,(o=this._$notifyConnectionChanged)===null||o===void 0||o.call(this,e);else throw new Error("part.setConnected() may only be called on a RootPart returned from render().")}}class Vt{constructor(e,o,i,n,s){this.type=Po,this._$committedValue=T,this._$disconnectableChildren=void 0,this.element=e,this.name=o,this._$parent=n,this.options=s,i.length>2||i[0]!==""||i[1]!==""?(this._$committedValue=new Array(i.length-1).fill(new String),this.strings=i):this._$committedValue=T,this._sanitizer=void 0}get tagName(){return this.element.tagName}get _$isConnected(){return this._$parent._$isConnected}_$setValue(e,o=this,i,n){const s=this.strings;let r=!1;if(s===void 0)e=Ve(this,e,o,0),r=!Ye(e)||e!==this._$committedValue&&e!==xe,r&&(this._$committedValue=e);else{const l=e;e=s[0];let a,d;for(a=0;a<s.length-1;a++)d=Ve(this,l[i+a],o,a),d===xe&&(d=this._$committedValue[a]),r||(r=!Ye(d)||d!==this._$committedValue[a]),d===T?e=T:e!==T&&(e+=(d??"")+s[a+1]),this._$committedValue[a]=d}r&&!n&&this._commitValue(e)}_commitValue(e){e===T?q(this.element).removeAttribute(this.name):(this._sanitizer===void 0&&(this._sanitizer=Ce(this.element,this.name,"attribute")),e=this._sanitizer(e??""),b==null||b({kind:"commit attribute",element:this.element,name:this.name,value:e,options:this.options}),q(this.element).setAttribute(this.name,e??""))}}class Vs extends Vt{constructor(){super(...arguments),this.type=Ns}_commitValue(e){this._sanitizer===void 0&&(this._sanitizer=Ce(this.element,this.name,"property")),e=this._sanitizer(e),b==null||b({kind:"commit property",element:this.element,name:this.name,value:e,options:this.options}),this.element[this.name]=e===T?void 0:e}}const Ds=Me?Me.emptyScript:"";class Us extends Vt{constructor(){super(...arguments),this.type=Is}_commitValue(e){b==null||b({kind:"commit boolean attribute",element:this.element,name:this.name,value:!!(e&&e!==T),options:this.options}),e&&e!==T?q(this.element).setAttribute(this.name,Ds):q(this.element).removeAttribute(this.name)}}class zs extends Vt{constructor(e,o,i,n,s){if(super(e,o,i,n,s),this.type=Ps,this.strings!==void 0)throw new Error(`A \`<${e.localName}>\` has a \`@${o}=...\` listener with invalid content. Event listeners in templates must have exactly one expression and no surrounding text.`)}_$setValue(e,o=this){var i;if(e=(i=Ve(this,e,o,0))!==null&&i!==void 0?i:T,e===xe)return;const n=this._$committedValue,s=e===T&&n!==T||e.capture!==n.capture||e.once!==n.once||e.passive!==n.passive,r=e!==T&&(n===T||s);b==null||b({kind:"commit event listener",element:this.element,name:this.name,value:e,options:this.options,removeListener:s,addListener:r,oldListener:n}),s&&this.element.removeEventListener(this.name,this,n),r&&this.element.addEventListener(this.name,this,e),this._$committedValue=e}handleEvent(e){var o,i;typeof this._$committedValue=="function"?this._$committedValue.call((i=(o=this.options)===null||o===void 0?void 0:o.host)!==null&&i!==void 0?i:this.element,e):this._$committedValue.handleEvent(e)}}class js{constructor(e,o,i){this.element=e,this.type=Oo,this._$disconnectableChildren=void 0,this._$parent=o,this.options=i}get _$isConnected(){return this._$parent._$isConnected}_$setValue(e){b==null||b({kind:"commit to element binding",element:this.element,value:e,options:this.options}),Ve(this,e)}}const no=F.litHtmlPolyfillSupportDevMode;no==null||no(Je,ot);((Zt=F.litHtmlVersions)!==null&&Zt!==void 0?Zt:F.litHtmlVersions=[]).push("2.8.0");F.litHtmlVersions.length>1&&xt("multiple-versions","Multiple versions of Lit loaded. Loading multiple versions is not recommended.");const we=(t,e,o)=>{var i,n;if(e==null)throw new TypeError(`The container to render into may not be ${e}`);const s=_s++,r=(i=o==null?void 0:o.renderBefore)!==null&&i!==void 0?i:e;let l=r._$litPart$;if(b==null||b({kind:"begin render",id:s,value:t,container:e,options:o,part:l}),l===void 0){const a=(n=o==null?void 0:o.renderBefore)!==null&&n!==void 0?n:null;r._$litPart$=l=new ot(e.insertBefore(Ke(),a),a,void 0,o??{})}return l._$setValue(t),b==null||b({kind:"end render",id:s,value:t,container:e,options:o,part:l}),l};we.setSanitizer=ws,we.createSanitizer=Eo,we._testOnlyClearSanitizerFactoryDoNotCallOrElse=Es;/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var so,ro,ao;let Lo;{const t=(so=globalThis.litIssuedWarnings)!==null&&so!==void 0?so:globalThis.litIssuedWarnings=new Set;Lo=(e,o)=>{o+=` See https://lit.dev/msg/${e} for more information.`,t.has(o)||(console.warn(o),t.add(o))}}class A extends ie{constructor(){super(...arguments),this.renderOptions={host:this},this.__childPart=void 0}createRenderRoot(){var e,o;const i=super.createRenderRoot();return(e=(o=this.renderOptions).renderBefore)!==null&&e!==void 0||(o.renderBefore=i.firstChild),i}update(e){const o=this.render();this.hasUpdated||(this.renderOptions.isConnected=this.isConnected),super.update(e),this.__childPart=we(o,this.renderRoot,this.renderOptions)}connectedCallback(){var e;super.connectedCallback(),(e=this.__childPart)===null||e===void 0||e.setConnected(!0)}disconnectedCallback(){var e;super.disconnectedCallback(),(e=this.__childPart)===null||e===void 0||e.setConnected(!1)}render(){return xe}}A.finalized=!0;A._$litElement$=!0;(ro=globalThis.litElementHydrateSupport)===null||ro===void 0||ro.call(globalThis,{LitElement:A});const lo=globalThis.litElementPolyfillSupportDevMode;lo==null||lo({LitElement:A});A.finalize=function(){if(!ie.finalize.call(this))return!1;const e=(o,i,n=!1)=>{if(o.hasOwnProperty(i)){const s=(typeof o=="function"?o:o.constructor).name;Lo(n?"renamed-api":"removed-api",`\`${i}\` is implemented on class ${s}. It has been ${n?"renamed":"removed"} in this version of LitElement.`)}};return e(this,"render"),e(this,"getStyles",!0),e(this.prototype,"adoptStyles"),!0};((ao=globalThis.litElementVersions)!==null&&ao!==void 0?ao:globalThis.litElementVersions=[]).push("3.3.3");globalThis.litElementVersions.length>1&&Lo("multiple-versions","Multiple versions of Lit loaded. Loading multiple versions is not recommended.");/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const Fs=(t,e)=>(customElements.define(t,e),e),Bs=(t,e)=>{const{kind:o,elements:i}=e;return{kind:o,elements:i,finisher(n){customElements.define(t,n)}}},V=t=>e=>typeof e=="function"?Fs(t,e):Bs(t,e);/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const Hs=(t,e)=>e.kind==="method"&&e.descriptor&&!("value"in e.descriptor)?{...e,finisher(o){o.createProperty(e.key,t)}}:{kind:"field",key:Symbol(),placement:"own",descriptor:{},originalKey:e.key,initializer(){typeof e.initializer=="function"&&(this[e.key]=e.initializer.call(this))},finisher(o){o.createProperty(e.key,t)}},qs=(t,e,o)=>{e.constructor.createProperty(o,t)};function w(t){return(e,o)=>o!==void 0?qs(t,e,o):Hs(t,e)}/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */function I(t){return w({...t,state:!0})}/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const Ws=({finisher:t,descriptor:e})=>(o,i)=>{var n;if(i!==void 0){const s=o.constructor;e!==void 0&&Object.defineProperty(o,i,e(i)),t==null||t(s,i)}else{const s=(n=o.originalKey)!==null&&n!==void 0?n:o.key,r=e!=null?{kind:"method",placement:"prototype",key:s,descriptor:e(o.key)}:{...o,key:s};return t!=null&&(r.finisher=function(l){t(l,s)}),r}};/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */function it(t,e){return Ws({descriptor:o=>{const i={get(){var n,s;return(s=(n=this.renderRoot)===null||n===void 0?void 0:n.querySelector(t))!==null&&s!==void 0?s:null},enumerable:!0,configurable:!0};if(e){const n=typeof o=="symbol"?Symbol():`__${o}`;i.get=function(){var s,r;return this[n]===void 0&&(this[n]=(r=(s=this.renderRoot)===null||s===void 0?void 0:s.querySelector(t))!==null&&r!==void 0?r:null),this[n]}}return i}})}/**
 * @license
 * Copyright 2021 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */var co;const Gs=window;((co=Gs.HTMLSlotElement)===null||co===void 0?void 0:co.prototype.assignedElements)!=null;/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const Ks={ATTRIBUTE:1,CHILD:2,PROPERTY:3,BOOLEAN_ATTRIBUTE:4,EVENT:5,ELEMENT:6},Ys=t=>(...e)=>({_$litDirective$:t,values:e});class Js{constructor(e){}get _$isConnected(){return this._$parent._$isConnected}_$initialize(e,o,i){this.__part=e,this._$parent=o,this.__attributeIndex=i}_$resolve(e,o){return this.update(e,o)}update(e,o){return this.render(...o)}}/**
 * @license
 * Copyright 2018 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */class Xs extends Js{constructor(e){var o;if(super(e),e.type!==Ks.ATTRIBUTE||e.name!=="class"||((o=e.strings)===null||o===void 0?void 0:o.length)>2)throw new Error("`classMap()` can only be used in the `class` attribute and must be the only part in the attribute.")}render(e){return" "+Object.keys(e).filter(o=>e[o]).join(" ")+" "}update(e,[o]){var i,n;if(this._previousClasses===void 0){this._previousClasses=new Set,e.strings!==void 0&&(this._staticClasses=new Set(e.strings.join(" ").split(/\s/).filter(r=>r!=="")));for(const r in o)o[r]&&!(!((i=this._staticClasses)===null||i===void 0)&&i.has(r))&&this._previousClasses.add(r);return this.render(o)}const s=e.element.classList;this._previousClasses.forEach(r=>{r in o||(s.remove(r),this._previousClasses.delete(r))});for(const r in o){const l=!!o[r];l!==this._previousClasses.has(r)&&!(!((n=this._staticClasses)===null||n===void 0)&&n.has(r))&&(l?(s.add(r),this._previousClasses.add(r)):(s.remove(r),this._previousClasses.delete(r)))}return xe}}const Mo=Ys(Xs),ho="css-loading-indicator";var K;(function(t){t.IDLE="",t.FIRST="first",t.SECOND="second",t.THIRD="third"})(K||(K={}));class O extends A{constructor(){super(),this.firstDelay=450,this.secondDelay=1500,this.thirdDelay=5e3,this.expandedDuration=2e3,this.onlineText="Online",this.offlineText="Connection lost",this.reconnectingText="Connection lost, trying to reconnect...",this.offline=!1,this.reconnecting=!1,this.expanded=!1,this.loading=!1,this.loadingBarState=K.IDLE,this.applyDefaultThemeState=!0,this.firstTimeout=0,this.secondTimeout=0,this.thirdTimeout=0,this.expandedTimeout=0,this.lastMessageState=R.CONNECTED,this.connectionStateListener=()=>{this.expanded=this.updateConnectionState(),this.expandedTimeout=this.timeoutFor(this.expandedTimeout,this.expanded,()=>{this.expanded=!1},this.expandedDuration)}}static create(){var e,o;const i=window;return!((e=i.Vaadin)===null||e===void 0)&&e.connectionIndicator||(i.Vaadin=i.Vaadin||{},i.Vaadin.connectionIndicator=document.createElement("vaadin-connection-indicator"),document.body.appendChild(i.Vaadin.connectionIndicator)),(o=i.Vaadin)===null||o===void 0?void 0:o.connectionIndicator}render(){return _`
      <div class="v-loading-indicator ${this.loadingBarState}" style=${this.getLoadingBarStyle()}></div>

      <div
        class="v-status-message ${Mo({active:this.reconnecting})}"
      >
        <span class="text"> ${this.renderMessage()} </span>
      </div>
    `}connectedCallback(){var e;super.connectedCallback();const o=window;!((e=o.Vaadin)===null||e===void 0)&&e.connectionState&&(this.connectionStateStore=o.Vaadin.connectionState,this.connectionStateStore.addStateChangeListener(this.connectionStateListener),this.updateConnectionState()),this.updateTheme()}disconnectedCallback(){super.disconnectedCallback(),this.connectionStateStore&&this.connectionStateStore.removeStateChangeListener(this.connectionStateListener),this.updateTheme()}get applyDefaultTheme(){return this.applyDefaultThemeState}set applyDefaultTheme(e){e!==this.applyDefaultThemeState&&(this.applyDefaultThemeState=e,this.updateTheme())}createRenderRoot(){return this}updateConnectionState(){var e;const o=(e=this.connectionStateStore)===null||e===void 0?void 0:e.state;return this.offline=o===R.CONNECTION_LOST,this.reconnecting=o===R.RECONNECTING,this.updateLoading(o===R.LOADING),this.loading?!1:o!==this.lastMessageState?(this.lastMessageState=o,!0):!1}updateLoading(e){this.loading=e,this.loadingBarState=K.IDLE,this.firstTimeout=this.timeoutFor(this.firstTimeout,e,()=>{this.loadingBarState=K.FIRST},this.firstDelay),this.secondTimeout=this.timeoutFor(this.secondTimeout,e,()=>{this.loadingBarState=K.SECOND},this.secondDelay),this.thirdTimeout=this.timeoutFor(this.thirdTimeout,e,()=>{this.loadingBarState=K.THIRD},this.thirdDelay)}renderMessage(){return this.reconnecting?this.reconnectingText:this.offline?this.offlineText:this.onlineText}updateTheme(){if(this.applyDefaultThemeState&&this.isConnected){if(!document.getElementById(ho)){const e=document.createElement("style");e.id=ho,e.textContent=this.getDefaultStyle(),document.head.appendChild(e)}}else{const e=document.getElementById(ho);e&&document.head.removeChild(e)}}getDefaultStyle(){return`
      @keyframes v-progress-start {
        0% {
          width: 0%;
        }
        100% {
          width: 50%;
        }
      }
      @keyframes v-progress-delay {
        0% {
          width: 50%;
        }
        100% {
          width: 90%;
        }
      }
      @keyframes v-progress-wait {
        0% {
          width: 90%;
          height: 4px;
        }
        3% {
          width: 91%;
          height: 7px;
        }
        100% {
          width: 96%;
          height: 7px;
        }
      }
      @keyframes v-progress-wait-pulse {
        0% {
          opacity: 1;
        }
        50% {
          opacity: 0.1;
        }
        100% {
          opacity: 1;
        }
      }
      .v-loading-indicator,
      .v-status-message {
        position: fixed;
        z-index: 251;
        left: 0;
        right: auto;
        top: 0;
        background-color: var(--lumo-primary-color, var(--material-primary-color, blue));
        transition: none;
      }
      .v-loading-indicator {
        width: 50%;
        height: 4px;
        opacity: 1;
        pointer-events: none;
        animation: v-progress-start 1000ms 200ms both;
      }
      .v-loading-indicator[style*='none'] {
        display: block !important;
        width: 100%;
        opacity: 0;
        animation: none;
        transition: opacity 500ms 300ms, width 300ms;
      }
      .v-loading-indicator.second {
        width: 90%;
        animation: v-progress-delay 3.8s forwards;
      }
      .v-loading-indicator.third {
        width: 96%;
        animation: v-progress-wait 5s forwards, v-progress-wait-pulse 1s 4s infinite backwards;
      }

      vaadin-connection-indicator[offline] .v-loading-indicator,
      vaadin-connection-indicator[reconnecting] .v-loading-indicator {
        display: none;
      }

      .v-status-message {
        opacity: 0;
        width: 100%;
        max-height: var(--status-height-collapsed, 8px);
        overflow: hidden;
        background-color: var(--status-bg-color-online, var(--lumo-primary-color, var(--material-primary-color, blue)));
        color: var(
          --status-text-color-online,
          var(--lumo-primary-contrast-color, var(--material-primary-contrast-color, #fff))
        );
        font-size: 0.75rem;
        font-weight: 600;
        line-height: 1;
        transition: all 0.5s;
        padding: 0 0.5em;
      }

      vaadin-connection-indicator[offline] .v-status-message,
      vaadin-connection-indicator[reconnecting] .v-status-message {
        opacity: 1;
        background-color: var(--status-bg-color-offline, var(--lumo-shade, #333));
        color: var(
          --status-text-color-offline,
          var(--lumo-primary-contrast-color, var(--material-primary-contrast-color, #fff))
        );
        background-image: repeating-linear-gradient(
          45deg,
          rgba(255, 255, 255, 0),
          rgba(255, 255, 255, 0) 10px,
          rgba(255, 255, 255, 0.1) 10px,
          rgba(255, 255, 255, 0.1) 20px
        );
      }

      vaadin-connection-indicator[reconnecting] .v-status-message {
        animation: show-reconnecting-status 2s;
      }

      vaadin-connection-indicator[offline] .v-status-message:hover,
      vaadin-connection-indicator[reconnecting] .v-status-message:hover,
      vaadin-connection-indicator[expanded] .v-status-message {
        max-height: var(--status-height, 1.75rem);
      }

      vaadin-connection-indicator[expanded] .v-status-message {
        opacity: 1;
      }

      .v-status-message span {
        display: flex;
        align-items: center;
        justify-content: center;
        height: var(--status-height, 1.75rem);
      }

      vaadin-connection-indicator[reconnecting] .v-status-message span::before {
        content: '';
        width: 1em;
        height: 1em;
        border-top: 2px solid
          var(--status-spinner-color, var(--lumo-primary-color, var(--material-primary-color, blue)));
        border-left: 2px solid
          var(--status-spinner-color, var(--lumo-primary-color, var(--material-primary-color, blue)));
        border-right: 2px solid transparent;
        border-bottom: 2px solid transparent;
        border-radius: 50%;
        box-sizing: border-box;
        animation: v-spin 0.4s linear infinite;
        margin: 0 0.5em;
      }

      @keyframes v-spin {
        100% {
          transform: rotate(360deg);
        }
      }
    `}getLoadingBarStyle(){switch(this.loadingBarState){case K.IDLE:return"display: none";case K.FIRST:case K.SECOND:case K.THIRD:return"display: block";default:return""}}timeoutFor(e,o,i,n){return e!==0&&window.clearTimeout(e),o?window.setTimeout(i,n):0}static get instance(){return O.create()}}B([w({type:Number})],O.prototype,"firstDelay",void 0);B([w({type:Number})],O.prototype,"secondDelay",void 0);B([w({type:Number})],O.prototype,"thirdDelay",void 0);B([w({type:Number})],O.prototype,"expandedDuration",void 0);B([w({type:String})],O.prototype,"onlineText",void 0);B([w({type:String})],O.prototype,"offlineText",void 0);B([w({type:String})],O.prototype,"reconnectingText",void 0);B([w({type:Boolean,reflect:!0})],O.prototype,"offline",void 0);B([w({type:Boolean,reflect:!0})],O.prototype,"reconnecting",void 0);B([w({type:Boolean,reflect:!0})],O.prototype,"expanded",void 0);B([w({type:Boolean,reflect:!0})],O.prototype,"loading",void 0);B([w({type:String})],O.prototype,"loadingBarState",void 0);B([w({type:Boolean})],O.prototype,"applyDefaultTheme",null);customElements.get("vaadin-connection-indicator")===void 0&&customElements.define("vaadin-connection-indicator",O);O.instance;const Xe=window;Xe.Vaadin=Xe.Vaadin||{};Xe.Vaadin.registrations=Xe.Vaadin.registrations||[];Xe.Vaadin.registrations.push({is:"@vaadin/common-frontend",version:"0.0.18"});class wi extends Error{}const He=window.document.body,C=window;class Qs{constructor(e){this.response=void 0,this.pathname="",this.isActive=!1,this.baseRegex=/^\//,this.navigation="",He.$=He.$||[],this.config=e||{},C.Vaadin=C.Vaadin||{},C.Vaadin.Flow=C.Vaadin.Flow||{},C.Vaadin.Flow.clients={TypeScript:{isActive:()=>this.isActive}};const o=document.head.querySelector("base");this.baseRegex=new RegExp(`^${(document.baseURI||o&&o.href||"/").replace(/^https?:\/\/[^/]+/i,"")}`),this.appShellTitle=document.title,this.addConnectionIndicator()}get serverSideRoutes(){return[{path:"(.*)",action:this.action}]}loadingStarted(){this.isActive=!0,C.Vaadin.connectionState.loadingStarted()}loadingFinished(){this.isActive=!1,C.Vaadin.connectionState.loadingFinished(),!C.Vaadin.listener&&(C.Vaadin.listener={},document.addEventListener("click",e=>{e.target&&(e.target.hasAttribute("router-link")?this.navigation="link":e.composedPath().some(o=>o.nodeName==="A")&&(this.navigation="client"))},{capture:!0}))}get action(){return async e=>{if(this.pathname=e.pathname,C.Vaadin.connectionState.online)try{await this.flowInit()}catch(o){if(o instanceof wi)return C.Vaadin.connectionState.state=R.CONNECTION_LOST,this.offlineStubAction();throw o}else return this.offlineStubAction();return this.container.onBeforeEnter=(o,i)=>this.flowNavigate(o,i),this.container.onBeforeLeave=(o,i)=>this.flowLeave(o,i),this.container}}async flowLeave(e,o){const{connectionState:i}=C.Vaadin;return this.pathname===e.pathname||!this.isFlowClientLoaded()||i.offline?Promise.resolve({}):new Promise(n=>{this.loadingStarted(),this.container.serverConnected=s=>{n(o&&s?o.prevent():{}),this.loadingFinished()},He.$server.leaveNavigation(this.getFlowRoutePath(e),this.getFlowRouteQuery(e))})}async flowNavigate(e,o){return this.response?new Promise(i=>{this.loadingStarted(),this.container.serverConnected=(n,s)=>{o&&n?i(o.prevent()):o&&o.redirect&&s?i(o.redirect(s.pathname)):(this.container.style.display="",i(this.container)),this.loadingFinished()},this.container.serverPaused=()=>{this.loadingFinished()},He.$server.connectClient(this.getFlowRoutePath(e),this.getFlowRouteQuery(e),this.appShellTitle,history.state,this.navigation),this.navigation="history"}):Promise.resolve(this.container)}getFlowRoutePath(e){return decodeURIComponent(e.pathname).replace(this.baseRegex,"")}getFlowRouteQuery(e){return e.search&&e.search.substring(1)||""}async flowInit(){if(!this.isFlowClientLoaded()){this.loadingStarted(),this.response=await this.flowInitUi();const{pushScript:e,appConfig:o}=this.response;typeof e=="string"&&await this.loadScript(e);const{appId:i}=o;await(await g(()=>import("./FlowBootstrap-feff2646.js"),[],import.meta.url)).init(this.response),typeof this.config.imports=="function"&&(this.injectAppIdScript(i),await this.config.imports());const s=`flow-container-${i.toLowerCase()}`,r=document.querySelector(s);r?this.container=r:(this.container=document.createElement(s),this.container.id=i),He.$[i]=this.container;const l=await g(()=>import("./FlowClient-341d667e.js"),[],import.meta.url);await this.flowInitClient(l),this.loadingFinished()}return this.container&&!this.container.isConnected&&(this.container.style.display="none",document.body.appendChild(this.container)),this.response}async loadScript(e){return new Promise((o,i)=>{const n=document.createElement("script");n.onload=()=>o(),n.onerror=i,n.src=e,document.body.appendChild(n)})}injectAppIdScript(e){const o=e.substring(0,e.lastIndexOf("-")),i=document.createElement("script");i.type="module",i.setAttribute("data-app-id",o),document.body.append(i)}async flowInitClient(e){return e.init(),new Promise(o=>{const i=setInterval(()=>{Object.keys(C.Vaadin.Flow.clients).filter(s=>s!=="TypeScript").reduce((s,r)=>s||C.Vaadin.Flow.clients[r].isActive(),!1)||(clearInterval(i),o())},5)})}async flowInitUi(){const e=C.Vaadin&&C.Vaadin.TypeScript&&C.Vaadin.TypeScript.initial;return e?(C.Vaadin.TypeScript.initial=void 0,Promise.resolve(e)):new Promise((o,i)=>{const s=new XMLHttpRequest,r=`?v-r=init&location=${encodeURIComponent(this.getFlowRoutePath(location))}&query=${encodeURIComponent(this.getFlowRouteQuery(location))}`;s.open("GET",r),s.onerror=()=>i(new wi(`Invalid server response when initializing Flow UI.
        ${s.status}
        ${s.responseText}`)),s.onload=()=>{const l=s.getResponseHeader("content-type");l&&l.indexOf("application/json")!==-1?o(JSON.parse(s.responseText)):s.onerror()},s.send()})}addConnectionIndicator(){O.create(),C.addEventListener("online",()=>{if(!this.isFlowClientLoaded()){C.Vaadin.connectionState.state=R.RECONNECTING;const e=new XMLHttpRequest;e.open("HEAD","sw.js"),e.onload=()=>{C.Vaadin.connectionState.state=R.CONNECTED},e.onerror=()=>{C.Vaadin.connectionState.state=R.CONNECTION_LOST},setTimeout(()=>e.send(),50)}}),C.addEventListener("offline",()=>{this.isFlowClientLoaded()||(C.Vaadin.connectionState.state=R.CONNECTION_LOST)})}async offlineStubAction(){const e=document.createElement("iframe"),o="./offline-stub.html";e.setAttribute("src",o),e.setAttribute("style","width: 100%; height: 100%; border: 0"),this.response=void 0;let i;const n=()=>{i!==void 0&&(C.Vaadin.connectionState.removeStateChangeListener(i),i=void 0)};return e.onBeforeEnter=(s,r,l)=>{i=()=>{C.Vaadin.connectionState.online&&(n(),l.render(s,!1))},C.Vaadin.connectionState.addStateChangeListener(i)},e.onBeforeLeave=(s,r,l)=>{n()},e}isFlowClientLoaded(){return this.response!==void 0}}const{serverSideRoutes:Zs}=new Qs({imports:()=>g(()=>import("./generated-flow-imports-a654aac9.js").then(t=>t.g),[],import.meta.url)}),er=[...Zs],tr=new pe(document.querySelector("#outlet"));tr.setRoutes(er);(function(){if(typeof document>"u"||"adoptedStyleSheets"in document)return;var t="ShadyCSS"in window&&!ShadyCSS.nativeShadow,e=document.implementation.createHTMLDocument(""),o=new WeakMap,i=typeof DOMException=="object"?Error:DOMException,n=Object.defineProperty,s=Array.prototype.forEach,r=/@import.+?;?$/gm;function l(c){var p=c.replace(r,"");return p!==c&&console.warn("@import rules are not allowed here. See https://github.com/WICG/construct-stylesheets/issues/119#issuecomment-588352418"),p.trim()}function a(c){return"isConnected"in c?c.isConnected:document.contains(c)}function d(c){return c.filter(function(p,E){return c.indexOf(p)===E})}function h(c,p){return c.filter(function(E){return p.indexOf(E)===-1})}function v(c){c.parentNode.removeChild(c)}function u(c){return c.shadowRoot||o.get(c)}var y=["addRule","deleteRule","insertRule","removeRule"],ce=CSSStyleSheet,he=ce.prototype;he.replace=function(){return Promise.reject(new i("Can't call replace on non-constructed CSSStyleSheets."))},he.replaceSync=function(){throw new i("Failed to execute 'replaceSync' on 'CSSStyleSheet': Can't call replaceSync on non-constructed CSSStyleSheets.")};function ne(c){return typeof c=="object"?$e.isPrototypeOf(c)||he.isPrototypeOf(c):!1}function Dt(c){return typeof c=="object"?he.isPrototypeOf(c):!1}var H=new WeakMap,ee=new WeakMap,ke=new WeakMap,Te=new WeakMap;function Ut(c,p){var E=document.createElement("style");return ke.get(c).set(p,E),ee.get(c).push(p),E}function se(c,p){return ke.get(c).get(p)}function nt(c,p){ke.get(c).delete(p),ee.set(c,ee.get(c).filter(function(E){return E!==p}))}function Fo(c,p){requestAnimationFrame(function(){p.textContent=H.get(c).textContent,Te.get(c).forEach(function(E){return p.sheet[E.method].apply(p.sheet,E.args)})})}function st(c){if(!H.has(c))throw new TypeError("Illegal invocation")}function zt(){var c=this,p=document.createElement("style");e.body.appendChild(p),H.set(c,p),ee.set(c,[]),ke.set(c,new WeakMap),Te.set(c,[])}var $e=zt.prototype;$e.replace=function(p){try{return this.replaceSync(p),Promise.resolve(this)}catch(E){return Promise.reject(E)}},$e.replaceSync=function(p){if(st(this),typeof p=="string"){var E=this;H.get(E).textContent=l(p),Te.set(E,[]),ee.get(E).forEach(function(D){D.isConnected()&&Fo(E,se(E,D))})}},n($e,"cssRules",{configurable:!0,enumerable:!0,get:function(){return st(this),H.get(this).sheet.cssRules}}),n($e,"media",{configurable:!0,enumerable:!0,get:function(){return st(this),H.get(this).sheet.media}}),y.forEach(function(c){$e[c]=function(){var p=this;st(p);var E=arguments;Te.get(p).push({method:c,args:E}),ee.get(p).forEach(function(z){if(z.isConnected()){var L=se(p,z).sheet;L[c].apply(L,E)}});var D=H.get(p).sheet;return D[c].apply(D,E)}}),n(zt,Symbol.hasInstance,{configurable:!0,value:ne});var Bo={childList:!0,subtree:!0},Ho=new WeakMap;function Ae(c){var p=Ho.get(c);return p||(p=new Go(c),Ho.set(c,p)),p}function qo(c){n(c.prototype,"adoptedStyleSheets",{configurable:!0,enumerable:!0,get:function(){return Ae(this).sheets},set:function(p){Ae(this).update(p)}})}function jt(c,p){for(var E=document.createNodeIterator(c,NodeFilter.SHOW_ELEMENT,function(z){return u(z)?NodeFilter.FILTER_ACCEPT:NodeFilter.FILTER_REJECT},null,!1),D=void 0;D=E.nextNode();)p(u(D))}var rt=new WeakMap,Re=new WeakMap,at=new WeakMap;function Sn(c,p){return p instanceof HTMLStyleElement&&Re.get(c).some(function(E){return se(E,c)})}function Wo(c){var p=rt.get(c);return p instanceof Document?p.body:p}function Ft(c){var p=document.createDocumentFragment(),E=Re.get(c),D=at.get(c),z=Wo(c);D.disconnect(),E.forEach(function(L){p.appendChild(se(L,c)||Ut(L,c))}),z.insertBefore(p,null),D.observe(z,Bo),E.forEach(function(L){Fo(L,se(L,c))})}function Go(c){var p=this;p.sheets=[],rt.set(p,c),Re.set(p,[]),at.set(p,new MutationObserver(function(E,D){if(!document){D.disconnect();return}E.forEach(function(z){t||s.call(z.addedNodes,function(L){L instanceof Element&&jt(L,function(Ne){Ae(Ne).connect()})}),s.call(z.removedNodes,function(L){L instanceof Element&&(Sn(p,L)&&Ft(p),t||jt(L,function(Ne){Ae(Ne).disconnect()}))})})}))}if(Go.prototype={isConnected:function(){var c=rt.get(this);return c instanceof Document?c.readyState!=="loading":a(c.host)},connect:function(){var c=Wo(this);at.get(this).observe(c,Bo),Re.get(this).length>0&&Ft(this),jt(c,function(p){Ae(p).connect()})},disconnect:function(){at.get(this).disconnect()},update:function(c){var p=this,E=rt.get(p)===document?"Document":"ShadowRoot";if(!Array.isArray(c))throw new TypeError("Failed to set the 'adoptedStyleSheets' property on "+E+": Iterator getter is not callable.");if(!c.every(ne))throw new TypeError("Failed to set the 'adoptedStyleSheets' property on "+E+": Failed to convert value to 'CSSStyleSheet'");if(c.some(Dt))throw new TypeError("Failed to set the 'adoptedStyleSheets' property on "+E+": Can't adopt non-constructed stylesheets");p.sheets=c;var D=Re.get(p),z=d(c),L=h(D,z);L.forEach(function(Ne){v(se(Ne,p)),nt(Ne,p)}),Re.set(p,z),p.isConnected()&&z.length>0&&Ft(p)}},window.CSSStyleSheet=zt,qo(Document),"ShadowRoot"in window){qo(ShadowRoot);var Ko=Element.prototype,xn=Ko.attachShadow;Ko.attachShadow=function(p){var E=xn.call(this,p);return p.mode==="closed"&&o.set(this,E),E}}var lt=Ae(document);lt.isConnected()?lt.connect():document.addEventListener("DOMContentLoaded",lt.connect.bind(lt))})();/**
 * @license
 * Copyright 2020 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */const mn=Symbol.for(""),or=t=>{if((t==null?void 0:t.r)===mn)return t==null?void 0:t._$litStatic$},ir=t=>{if(t._$litStatic$!==void 0)return t._$litStatic$;throw new Error(`Value passed to 'literal' function must be a 'literal' result: ${t}. Use 'unsafeStatic' to pass non-literal values, but
            take care to ensure page security.`)},ht=(t,...e)=>({_$litStatic$:e.reduce((o,i,n)=>o+ir(i)+t[n+1],t[0]),r:mn}),Ei=new Map,nr=t=>(e,...o)=>{const i=o.length;let n,s;const r=[],l=[];let a=0,d=!1,h;for(;a<i;){for(h=e[a];a<i&&(s=o[a],(n=or(s))!==void 0);)h+=n+e[++a],d=!0;a!==i&&l.push(s),r.push(h),a++}if(a===i&&r.push(e[i]),d){const v=r.join("$$lit$$");e=Ei.get(v),e===void 0&&(r.raw=r,Ei.set(v,e=r)),o=l}return t(e,...o)},sr=nr(_),rr="modulepreload",ar=function(t){return"/"+t},Si={},f=function(t,e,o){if(!e||e.length===0)return t();const i=document.getElementsByTagName("link");return Promise.all(e.map(n=>{if(n=ar(n),n in Si)return;Si[n]=!0;const s=n.endsWith(".css"),r=s?'[rel="stylesheet"]':"";if(o)for(let a=i.length-1;a>=0;a--){const d=i[a];if(d.href===n&&(!s||d.rel==="stylesheet"))return}else if(document.querySelector(`link[href="${n}"]${r}`))return;const l=document.createElement("link");if(l.rel=s?"stylesheet":rr,s||(l.as="script",l.crossOrigin=""),l.href=n,document.head.appendChild(l),s)return new Promise((a,d)=>{l.addEventListener("load",a),l.addEventListener("error",()=>d(new Error(`Unable to preload CSS for ${n}`)))})})).then(()=>t()).catch(n=>{const s=new Event("vite:preloadError",{cancelable:!0});if(s.payload=n,window.dispatchEvent(s),!s.defaultPrevented)throw n})};function m(t,e,o,i){var n=arguments.length,s=n<3?e:i===null?i=Object.getOwnPropertyDescriptor(e,o):i,r;if(typeof Reflect=="object"&&typeof Reflect.decorate=="function")s=Reflect.decorate(t,e,o,i);else for(var l=t.length-1;l>=0;l--)(r=t[l])&&(s=(n<3?r(s):n>3?r(e,o,s):r(e,o))||s);return n>3&&s&&Object.defineProperty(e,o,s),s}function lr(t){var e;const o=[];for(;t&&t.parentNode;){const i=xo(t);if(i.nodeId!==-1){if((e=i.element)!=null&&e.tagName.startsWith("FLOW-CONTAINER-"))break;o.push(i)}t=t.parentElement?t.parentElement:t.parentNode.host}return o.reverse()}function xo(t){const e=window.Vaadin;if(e&&e.Flow){const{clients:o}=e.Flow,i=Object.keys(o);for(const n of i){const s=o[n];if(s.getNodeId){const r=s.getNodeId(t);if(r>=0)return{nodeId:r,uiId:s.getUIId(),element:t}}}}return{nodeId:-1,uiId:-1,element:void 0}}function dr(t,e){if(t.contains(e))return!0;let o=e;const i=e.ownerDocument;for(;o&&o!==i&&o!==t;)o=o.parentNode||(o instanceof ShadowRoot?o.host:null);return o===t}const cr=(t,e)=>{const o=t[e];return o?typeof o=="function"?o():Promise.resolve(o):new Promise((i,n)=>{(typeof queueMicrotask=="function"?queueMicrotask:setTimeout)(n.bind(null,new Error("Unknown variable dynamic import: "+e)))})};var N;(function(t){t.text="text",t.checkbox="checkbox",t.range="range",t.color="color"})(N||(N={}));const Z={lumoSize:["--lumo-size-xs","--lumo-size-s","--lumo-size-m","--lumo-size-l","--lumo-size-xl"],lumoSpace:["--lumo-space-xs","--lumo-space-s","--lumo-space-m","--lumo-space-l","--lumo-space-xl"],lumoBorderRadius:["0","--lumo-border-radius-m","--lumo-border-radius-l"],lumoFontSize:["--lumo-font-size-xxs","--lumo-font-size-xs","--lumo-font-size-s","--lumo-font-size-m","--lumo-font-size-l","--lumo-font-size-xl","--lumo-font-size-xxl","--lumo-font-size-xxxl"],lumoTextColor:["--lumo-header-text-color","--lumo-body-text-color","--lumo-secondary-text-color","--lumo-tertiary-text-color","--lumo-disabled-text-color","--lumo-primary-text-color","--lumo-error-text-color","--lumo-success-text-color"],basicBorderSize:["0px","1px","2px","3px"]},hr=Object.freeze(Object.defineProperty({__proto__:null,presets:Z},Symbol.toStringTag,{value:"Module"})),ae={textColor:{propertyName:"color",displayName:"Text color",editorType:N.color,presets:Z.lumoTextColor},fontSize:{propertyName:"font-size",displayName:"Font size",editorType:N.range,presets:Z.lumoFontSize,icon:"font"},fontWeight:{propertyName:"font-weight",displayName:"Bold",editorType:N.checkbox,checkedValue:"bold"},fontStyle:{propertyName:"font-style",displayName:"Italic",editorType:N.checkbox,checkedValue:"italic"}},X={backgroundColor:{propertyName:"background-color",displayName:"Background color",editorType:N.color},borderColor:{propertyName:"border-color",displayName:"Border color",editorType:N.color},borderWidth:{propertyName:"border-width",displayName:"Border width",editorType:N.range,presets:Z.basicBorderSize,icon:"square"},borderRadius:{propertyName:"border-radius",displayName:"Border radius",editorType:N.range,presets:Z.lumoBorderRadius,icon:"square"},padding:{propertyName:"padding",displayName:"Padding",editorType:N.range,presets:Z.lumoSpace,icon:"square"},gap:{propertyName:"gap",displayName:"Spacing",editorType:N.range,presets:Z.lumoSpace,icon:"square"}},ur={height:{propertyName:"height",displayName:"Size",editorType:N.range,presets:Z.lumoSize,icon:"square"},paddingInline:{propertyName:"padding-inline",displayName:"Padding",editorType:N.range,presets:Z.lumoSpace,icon:"square"}},Co={iconColor:{propertyName:"color",displayName:"Icon color",editorType:N.color,presets:Z.lumoTextColor},iconSize:{propertyName:"font-size",displayName:"Icon size",editorType:N.range,presets:Z.lumoFontSize,icon:"font"}},pr=[X.backgroundColor,X.borderColor,X.borderWidth,X.borderRadius,X.padding],mr=[ae.textColor,ae.fontSize,ae.fontWeight,ae.fontStyle],vr=[Co.iconColor,Co.iconSize],fr=Object.freeze(Object.defineProperty({__proto__:null,fieldProperties:ur,iconProperties:Co,shapeProperties:X,standardIconProperties:vr,standardShapeProperties:pr,standardTextProperties:mr,textProperties:ae},Symbol.toStringTag,{value:"Module"}));function vn(t){const e=t.charAt(0).toUpperCase()+t.slice(1);return{tagName:t,displayName:e,elements:[{selector:t,displayName:"Element",properties:[X.backgroundColor,X.borderColor,X.borderWidth,X.borderRadius,X.padding,ae.textColor,ae.fontSize,ae.fontWeight,ae.fontStyle]}]}}const gr=Object.freeze(Object.defineProperty({__proto__:null,createGenericMetadata:vn},Symbol.toStringTag,{value:"Module"})),yr=t=>cr(Object.assign({"./components/defaults.ts":()=>f(()=>Promise.resolve().then(()=>fr),void 0),"./components/generic.ts":()=>f(()=>Promise.resolve().then(()=>gr),void 0),"./components/presets.ts":()=>f(()=>Promise.resolve().then(()=>hr),void 0),"./components/vaadin-accordion-heading.ts":()=>f(()=>g(()=>import("./vaadin-accordion-heading-c0acdd6d-af7db8ce.js"),[],import.meta.url),[]),"./components/vaadin-accordion-panel.ts":()=>f(()=>g(()=>import("./vaadin-accordion-panel-616e55d6-81d53fc2.js"),[],import.meta.url),[]),"./components/vaadin-accordion.ts":()=>f(()=>g(()=>import("./vaadin-accordion-eed3b794-67c087bd.js"),[],import.meta.url),[]),"./components/vaadin-app-layout.ts":()=>f(()=>g(()=>import("./vaadin-app-layout-e56de2e9-edb497a1.js"),[],import.meta.url),[]),"./components/vaadin-avatar.ts":()=>f(()=>g(()=>import("./vaadin-avatar-7599297d-d9b3720b.js"),[],import.meta.url),[]),"./components/vaadin-big-decimal-field.ts":()=>f(()=>g(()=>import("./vaadin-big-decimal-field-e51def24-a19d1fdb.js"),["./vaadin-big-decimal-field-e51def24-a19d1fdb.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-big-decimal-field-e51def24.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-board-row.ts":()=>f(()=>g(()=>import("./vaadin-board-row-c70d0c55-cb3a8991.js"),[],import.meta.url),[]),"./components/vaadin-board.ts":()=>f(()=>g(()=>import("./vaadin-board-828ebdea-64c931fa.js"),[],import.meta.url),[]),"./components/vaadin-button.ts":()=>f(()=>g(()=>import("./vaadin-button-2511ad84-b472ed91.js"),[],import.meta.url),[]),"./components/vaadin-chart.ts":()=>f(()=>g(()=>import("./vaadin-chart-5192dc15-9652cfd1.js"),[],import.meta.url),[]),"./components/vaadin-checkbox-group.ts":()=>f(()=>g(()=>import("./vaadin-checkbox-group-a7c65bf2-e226c686.js"),["./vaadin-checkbox-group-a7c65bf2-e226c686.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-checkbox-4e68df64-32dfc277.js"],import.meta.url),["assets/vaadin-checkbox-group-a7c65bf2.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-checkbox-4e68df64.js"]),"./components/vaadin-checkbox.ts":()=>f(()=>g(()=>import("./vaadin-checkbox-4e68df64-32dfc277.js"),[],import.meta.url),[]),"./components/vaadin-combo-box.ts":()=>f(()=>g(()=>import("./vaadin-combo-box-96451ddd-a867ee32.js"),["./vaadin-combo-box-96451ddd-a867ee32.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-combo-box-96451ddd.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-confirm-dialog.ts":()=>f(()=>g(()=>import("./vaadin-confirm-dialog-4d718829-f4d927f9.js"),["./vaadin-confirm-dialog-4d718829-f4d927f9.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-confirm-dialog-4d718829.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-cookie-consent.ts":()=>f(()=>g(()=>import("./vaadin-cookie-consent-46c09f8b-5764d5e6.js"),[],import.meta.url),[]),"./components/vaadin-crud.ts":()=>f(()=>g(()=>import("./vaadin-crud-8d161a22-af52555f.js"),[],import.meta.url),[]),"./components/vaadin-custom-field.ts":()=>f(()=>g(()=>import("./vaadin-custom-field-42c85b9e-e955ee18.js"),["./vaadin-custom-field-42c85b9e-e955ee18.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-custom-field-42c85b9e.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-date-picker.ts":()=>f(()=>g(()=>import("./vaadin-date-picker-f2001167-385eb09c.js"),["./vaadin-date-picker-f2001167-385eb09c.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-date-picker-f2001167.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-date-time-picker.ts":()=>f(()=>g(()=>import("./vaadin-date-time-picker-c8c047a7-a6f81819.js"),["./vaadin-date-time-picker-c8c047a7-a6f81819.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-date-time-picker-c8c047a7.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-details-summary.ts":()=>f(()=>g(()=>import("./vaadin-details-summary-351a1448-f8f89a73.js"),[],import.meta.url),[]),"./components/vaadin-details.ts":()=>f(()=>g(()=>import("./vaadin-details-bf336660-b5a94d60.js"),[],import.meta.url),[]),"./components/vaadin-dialog.ts":()=>f(()=>g(()=>import("./vaadin-dialog-53253a08-3aede1fc.js"),[],import.meta.url),[]),"./components/vaadin-email-field.ts":()=>f(()=>g(()=>import("./vaadin-email-field-d7a35f04-abab1b1a.js"),["./vaadin-email-field-d7a35f04-abab1b1a.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-email-field-d7a35f04.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-form-layout.ts":()=>f(()=>g(()=>import("./vaadin-form-layout-47744b1d-3da8d7f3.js"),[],import.meta.url),[]),"./components/vaadin-grid-pro.ts":()=>f(()=>g(()=>import("./vaadin-grid-pro-ff415555-5e93d46c.js"),["./vaadin-grid-pro-ff415555-5e93d46c.js","./vaadin-checkbox-4e68df64-32dfc277.js","./vaadin-grid-0a4791c2-20759722.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-grid-pro-ff415555.js","assets/vaadin-checkbox-4e68df64.js","assets/vaadin-grid-0a4791c2.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-grid.ts":()=>f(()=>g(()=>import("./vaadin-grid-0a4791c2-20759722.js"),["./vaadin-grid-0a4791c2-20759722.js","./vaadin-checkbox-4e68df64-32dfc277.js"],import.meta.url),["assets/vaadin-grid-0a4791c2.js","assets/vaadin-checkbox-4e68df64.js"]),"./components/vaadin-horizontal-layout.ts":()=>f(()=>g(()=>import("./vaadin-horizontal-layout-3193943f-5e139ed5.js"),[],import.meta.url),[]),"./components/vaadin-icon.ts":()=>f(()=>g(()=>import("./vaadin-icon-601f36ed-a07126b9.js"),[],import.meta.url),[]),"./components/vaadin-integer-field.ts":()=>f(()=>g(()=>import("./vaadin-integer-field-85078932-f7fe7620.js"),["./vaadin-integer-field-85078932-f7fe7620.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-integer-field-85078932.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-list-box.ts":()=>f(()=>g(()=>import("./vaadin-list-box-d7a8433b-87c19597.js"),[],import.meta.url),[]),"./components/vaadin-login-form.ts":()=>f(()=>g(()=>import("./vaadin-login-form-638996c6-454c2056.js"),["./vaadin-login-form-638996c6-454c2056.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-login-form-638996c6.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-login-overlay.ts":()=>f(()=>g(()=>import("./vaadin-login-overlay-f8a5db8a-a31b2446.js"),["./vaadin-login-overlay-f8a5db8a-a31b2446.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-login-overlay-f8a5db8a.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-map.ts":()=>f(()=>g(()=>import("./vaadin-map-d40a0116-dce5de65.js"),[],import.meta.url),[]),"./components/vaadin-menu-bar.ts":()=>f(()=>g(()=>import("./vaadin-menu-bar-3f5ab096-e642db30.js"),[],import.meta.url),[]),"./components/vaadin-message-input.ts":()=>f(()=>g(()=>import("./vaadin-message-input-996ac37c-32c398de.js"),["./vaadin-message-input-996ac37c-32c398de.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-message-input-996ac37c.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-message-list.ts":()=>f(()=>g(()=>import("./vaadin-message-list-70a435ba-e67e63df.js"),[],import.meta.url),[]),"./components/vaadin-multi-select-combo-box.ts":()=>f(()=>g(()=>import("./vaadin-multi-select-combo-box-a3373557-783f1848.js"),["./vaadin-multi-select-combo-box-a3373557-783f1848.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-multi-select-combo-box-a3373557.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-notification.ts":()=>f(()=>g(()=>import("./vaadin-notification-bd6eb776-5fb17d51.js"),[],import.meta.url),[]),"./components/vaadin-number-field.ts":()=>f(()=>g(()=>import("./vaadin-number-field-cb3ee8b2-366672a5.js"),["./vaadin-number-field-cb3ee8b2-366672a5.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-number-field-cb3ee8b2.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-password-field.ts":()=>f(()=>g(()=>import("./vaadin-password-field-d289cb18-402cff0c.js"),["./vaadin-password-field-d289cb18-402cff0c.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-password-field-d289cb18.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-progress-bar.ts":()=>f(()=>g(()=>import("./vaadin-progress-bar-309ecf1f-cb279142.js"),[],import.meta.url),[]),"./components/vaadin-radio-group.ts":()=>f(()=>g(()=>import("./vaadin-radio-group-88b5afd8-5440c26d.js"),["./vaadin-radio-group-88b5afd8-5440c26d.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-radio-group-88b5afd8.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-rich-text-editor.ts":()=>f(()=>g(()=>import("./vaadin-rich-text-editor-8cd892f2-a4cdc96e.js"),[],import.meta.url),[]),"./components/vaadin-scroller.ts":()=>f(()=>g(()=>import("./vaadin-scroller-35e68818-a04603cd.js"),[],import.meta.url),[]),"./components/vaadin-select.ts":()=>f(()=>g(()=>import("./vaadin-select-df6e9947-973424d1.js"),["./vaadin-select-df6e9947-973424d1.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-select-df6e9947.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-side-nav-item.ts":()=>f(()=>g(()=>import("./vaadin-side-nav-item-34918f92-506fb2a5.js"),[],import.meta.url),[]),"./components/vaadin-side-nav.ts":()=>f(()=>g(()=>import("./vaadin-side-nav-ba80d91d-cabfeec4.js"),[],import.meta.url),[]),"./components/vaadin-split-layout.ts":()=>f(()=>g(()=>import("./vaadin-split-layout-80c92131-5337986d.js"),[],import.meta.url),[]),"./components/vaadin-spreadsheet.ts":()=>f(()=>g(()=>import("./vaadin-spreadsheet-59d8c5ef-228b657e.js"),[],import.meta.url),[]),"./components/vaadin-tab.ts":()=>f(()=>g(()=>import("./vaadin-tab-aaf32809-0785931e.js"),[],import.meta.url),[]),"./components/vaadin-tabs.ts":()=>f(()=>g(()=>import("./vaadin-tabs-d9a5e24e-5a89530c.js"),[],import.meta.url),[]),"./components/vaadin-tabsheet.ts":()=>f(()=>g(()=>import("./vaadin-tabsheet-dd99ed9a-09f31613.js"),[],import.meta.url),[]),"./components/vaadin-text-area.ts":()=>f(()=>g(()=>import("./vaadin-text-area-83627ebc-cf49ade8.js"),["./vaadin-text-area-83627ebc-cf49ade8.js","./vaadin-text-field-0b3db014-784ce918.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-text-area-83627ebc.js","assets/vaadin-text-field-0b3db014.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-text-field.ts":()=>f(()=>g(()=>import("./vaadin-text-field-0b3db014-784ce918.js"),[],import.meta.url),[]),"./components/vaadin-time-picker.ts":()=>f(()=>g(()=>import("./vaadin-time-picker-715ec415-9deee89a.js"),["./vaadin-time-picker-715ec415-9deee89a.js","./vaadin-text-field-0b3db014-784ce918.js"],import.meta.url),["assets/vaadin-time-picker-715ec415.js","assets/vaadin-text-field-0b3db014.js"]),"./components/vaadin-upload.ts":()=>f(()=>g(()=>import("./vaadin-upload-d3c162ed-a1d6f270.js"),["./vaadin-upload-d3c162ed-a1d6f270.js","./vaadin-button-2511ad84-b472ed91.js"],import.meta.url),["assets/vaadin-upload-d3c162ed.js","assets/vaadin-button-2511ad84.js"]),"./components/vaadin-vertical-layout.ts":()=>f(()=>g(()=>import("./vaadin-vertical-layout-ad4174c4-6fb9f525.js"),[],import.meta.url),[]),"./components/vaadin-virtual-list.ts":()=>f(()=>g(()=>import("./vaadin-virtual-list-96896203-b45d7370.js"),[],import.meta.url),[])}),`./components/${t}.ts`);class _r{constructor(e=yr){this.loader=e,this.metadata={}}async getMetadata(e){var o;const i=(o=e.element)==null?void 0:o.localName;if(!i)return null;if(!i.startsWith("vaadin-"))return vn(i);let n=this.metadata[i];if(n)return n;try{n=(await this.loader(i)).default,this.metadata[i]=n}catch{console.warn(`Failed to load metadata for component: ${i}`)}return n||null}}const br=new _r,_t={crosshair:Ie`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M4 8v-2a2 2 0 0 1 2 -2h2"></path>
   <path d="M4 16v2a2 2 0 0 0 2 2h2"></path>
   <path d="M16 4h2a2 2 0 0 1 2 2v2"></path>
   <path d="M16 20h2a2 2 0 0 0 2 -2v-2"></path>
   <path d="M9 12l6 0"></path>
   <path d="M12 9l0 6"></path>
</svg>`,square:Ie`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="currentColor" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M3 3m0 2a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v14a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2z"></path>
</svg>`,font:Ie`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M4 20l3 0"></path>
   <path d="M14 20l7 0"></path>
   <path d="M6.9 15l6.9 0"></path>
   <path d="M10.2 6.3l5.8 13.7"></path>
   <path d="M5 20l6 -16l2 0l7 16"></path>
</svg>`,undo:Ie`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M9 13l-4 -4l4 -4m-4 4h11a4 4 0 0 1 0 8h-1"></path>
</svg>`,redo:Ie`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M15 13l4 -4l-4 -4m4 4h-11a4 4 0 0 0 0 8h1"></path>
</svg>`,cross:Ie`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="3" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M18 6l-12 12"></path>
   <path d="M6 6l12 12"></path>
</svg>`};var De;(function(t){t.disabled="disabled",t.enabled="enabled",t.missing_theme="missing_theme"})(De||(De={}));var P;(function(t){t.local="local",t.global="global"})(P||(P={}));function uo(t,e){return`${t}|${e}`}class ve{constructor(e){this._properties={},this._metadata=e}get metadata(){return this._metadata}get properties(){return Object.values(this._properties)}getPropertyValue(e,o){return this._properties[uo(e,o)]||null}updatePropertyValue(e,o,i,n){if(!i){delete this._properties[uo(e,o)];return}let s=this.getPropertyValue(e,o);s?(s.value=i,s.modified=n||!1):(s={elementSelector:e,propertyName:o,value:i,modified:n||!1},this._properties[uo(e,o)]=s)}addPropertyValues(e){e.forEach(o=>{this.updatePropertyValue(o.elementSelector,o.propertyName,o.value,o.modified)})}getPropertyValuesForElement(e){return this.properties.filter(o=>o.elementSelector===e)}static combine(...e){if(e.length<2)throw new Error("Must provide at least two themes");const o=new ve(e[0].metadata);return e.forEach(i=>o.addPropertyValues(i.properties)),o}static fromServerRules(e,o,i){const n=new ve(e);return e.elements.forEach(s=>{const r=Ue(s,o),l=i.find(a=>a.selector===r.replace(/ > /g,">"));l&&s.properties.forEach(a=>{const d=l.properties[a.propertyName];d&&n.updatePropertyValue(s.selector,a.propertyName,d,!0)})}),n}}function Ue(t,e){const o=t.selector;if(e.themeScope===P.global)return o;if(!e.localClassName)throw new Error("Can not build local scoped selector without instance class name");const i=o.match(/^[\w\d-_]+/),n=i&&i[0];if(!n)throw new Error(`Selector does not start with a tag name: ${o}`);return`${n}.${e.localClassName}${o.substring(n.length,o.length)}`}function wr(t,e,o,i){const n=Ue(t,e),s={[o]:i};return o==="border-width"&&(parseInt(i)>0?s["border-style"]="solid":s["border-style"]=""),{selector:n,properties:s}}function Er(t){const e=Object.entries(t.properties).map(([o,i])=>`${o}: ${i};`).join(" ");return`${t.selector} { ${e} }`}let ut,xi="";function Vo(t){ut||(ut=new CSSStyleSheet,document.adoptedStyleSheets=[...document.adoptedStyleSheets,ut]),xi+=t.cssText,ut.replaceSync(xi)}const fn=$`
  .editor-row {
    display: flex;
    align-items: baseline;
    padding: var(--theme-editor-section-horizontal-padding);
    gap: 10px;
  }

  .editor-row > .label {
    flex: 0 0 auto;
    width: 120px;
  }

  .editor-row > .editor {
    flex: 1 1 0;
  }
`,Ci="__vaadin-theme-editor-measure-element",ki=/((::before)|(::after))$/,Ti=/::part\(([\w\d_-]+)\)$/;Vo($`
  .__vaadin-theme-editor-measure-element {
    position: absolute;
    top: 0;
    left: 0;
    visibility: hidden;
  }
`);async function Sr(t){const e=new ve(t),o=document.createElement(t.tagName);o.classList.add(Ci),document.body.append(o),t.setupElement&&await t.setupElement(o);const i={themeScope:P.local,localClassName:Ci};try{t.elements.forEach(n=>{$i(o,n,i,!0);let s=Ue(n,i);const r=s.match(ki);s=s.replace(ki,"");const l=s.match(Ti),a=s.replace(Ti,"");let d=document.querySelector(a);if(d&&l){const u=`[part~="${l[1]}"]`;d=d.shadowRoot.querySelector(u)}if(!d)return;d.style.transition="none";const h=r?r[1]:null,v=getComputedStyle(d,h);n.properties.forEach(u=>{const y=v.getPropertyValue(u.propertyName)||u.defaultValue||"";e.updatePropertyValue(n.selector,u.propertyName,y)}),$i(o,n,i,!1)})}finally{try{t.cleanupElement&&await t.cleanupElement(o)}finally{o.remove()}}return e}function $i(t,e,o,i){if(e.stateAttribute){if(e.stateElementSelector){const n=Ue({...e,selector:e.stateElementSelector},o);t=document.querySelector(n)}t&&(i?t.setAttribute(e.stateAttribute,""):t.removeAttribute(e.stateAttribute))}}function Ai(t){return t.trim()}function xr(t){const e=t.element;if(!e)return null;const o=e.querySelector("label");if(o&&o.textContent)return Ai(o.textContent);const i=e.textContent;return i?Ai(i):null}class Cr{constructor(){this._localClassNameMap=new Map}get stylesheet(){return this.ensureStylesheet(),this._stylesheet}add(e){this.ensureStylesheet(),this._stylesheet.replaceSync(e)}clear(){this.ensureStylesheet(),this._stylesheet.replaceSync("")}previewLocalClassName(e,o){if(!e)return;const i=this._localClassNameMap.get(e);i&&(e.classList.remove(i),e.overlayClass=null),o?(e.classList.add(o),e.overlayClass=o,this._localClassNameMap.set(e,o)):this._localClassNameMap.delete(e)}ensureStylesheet(){this._stylesheet||(this._stylesheet=new CSSStyleSheet,this._stylesheet.replaceSync(""),document.adoptedStyleSheets=[...document.adoptedStyleSheets,this._stylesheet])}}const ye=new Cr;var Y;(function(t){t.response="themeEditorResponse",t.loadComponentMetadata="themeEditorComponentMetadata",t.setLocalClassName="themeEditorLocalClassName",t.setCssRules="themeEditorRules",t.loadRules="themeEditorLoadRules",t.history="themeEditorHistory",t.openCss="themeEditorOpenCss",t.markAsUsed="themeEditorMarkAsUsed"})(Y||(Y={}));var ko;(function(t){t.ok="ok",t.error="error"})(ko||(ko={}));class kr{constructor(e){this.pendingRequests={},this.requestCounter=0,this.wrappedConnection=e;const o=this.wrappedConnection.onMessage;this.wrappedConnection.onMessage=i=>{i.command===Y.response?this.handleResponse(i.data):o.call(this.wrappedConnection,i)}}sendRequest(e,o){const i=(this.requestCounter++).toString(),n=o.uiId??this.getGlobalUiId();return new Promise((s,r)=>{this.wrappedConnection.send(e,{...o,requestId:i,uiId:n}),this.pendingRequests[i]={resolve:s,reject:r}})}handleResponse(e){const o=this.pendingRequests[e.requestId];if(!o){console.warn("Received response for unknown request");return}delete this.pendingRequests[e.requestId],e.code===ko.ok?o.resolve(e):o.reject(e)}loadComponentMetadata(e){return this.sendRequest(Y.loadComponentMetadata,{nodeId:e.nodeId})}setLocalClassName(e,o){return this.sendRequest(Y.setLocalClassName,{nodeId:e.nodeId,className:o})}setCssRules(e){return this.sendRequest(Y.setCssRules,{rules:e})}loadRules(e){return this.sendRequest(Y.loadRules,{selectors:e})}markAsUsed(){return this.sendRequest(Y.markAsUsed,{})}undo(e){return this.sendRequest(Y.history,{undo:e})}redo(e){return this.sendRequest(Y.history,{redo:e})}openCss(e){return this.sendRequest(Y.openCss,{selector:e})}getGlobalUiId(){if(this.globalUiId===void 0){const e=window.Vaadin;if(e&&e.Flow){const{clients:o}=e.Flow,i=Object.keys(o);for(const n of i){const s=o[n];if(s.getNodeId){this.globalUiId=s.getUIId();break}}}}return this.globalUiId??-1}}const M={index:-1,entries:[]};class Tr{constructor(e){this.api=e}get allowUndo(){return M.index>=0}get allowRedo(){return M.index<M.entries.length-1}get allowedActions(){return{allowUndo:this.allowUndo,allowRedo:this.allowRedo}}push(e,o,i){const n={requestId:e,execute:o,rollback:i};if(M.index++,M.entries=M.entries.slice(0,M.index),M.entries.push(n),o)try{o()}catch(s){console.error("Execute history entry failed",s)}return this.allowedActions}async undo(){if(!this.allowUndo)return this.allowedActions;const e=M.entries[M.index];M.index--;try{await this.api.undo(e.requestId),e.rollback&&e.rollback()}catch(o){console.error("Undo failed",o)}return this.allowedActions}async redo(){if(!this.allowRedo)return this.allowedActions;M.index++;const e=M.entries[M.index];try{await this.api.redo(e.requestId),e.execute&&e.execute()}catch(o){console.error("Redo failed",o)}return this.allowedActions}static clear(){M.entries=[],M.index=-1}}class $r extends CustomEvent{constructor(e,o,i){super("theme-property-value-change",{bubbles:!0,composed:!0,detail:{element:e,property:o,value:i}})}}class W extends A{constructor(){super(...arguments),this.value=""}static get styles(){return[fn,$`
        :host {
          display: block;
        }

        .editor-row .label .modified {
          display: inline-block;
          width: 6px;
          height: 6px;
          background: orange;
          border-radius: 3px;
          margin-left: 3px;
        }
      `]}update(e){super.update(e),(e.has("propertyMetadata")||e.has("theme"))&&this.updateValueFromTheme()}render(){var e;return _`
      <div class="editor-row">
        <div class="label">
          ${this.propertyMetadata.displayName}
          ${(e=this.propertyValue)!=null&&e.modified?_`<span class="modified"></span>`:null}
        </div>
        <div class="editor">${this.renderEditor()}</div>
      </div>
    `}updateValueFromTheme(){var e;this.propertyValue=this.theme.getPropertyValue(this.elementMetadata.selector,this.propertyMetadata.propertyName),this.value=((e=this.propertyValue)==null?void 0:e.value)||""}dispatchChange(e){this.dispatchEvent(new $r(this.elementMetadata,this.propertyMetadata,e))}}m([w({})],W.prototype,"elementMetadata",void 0);m([w({})],W.prototype,"propertyMetadata",void 0);m([w({})],W.prototype,"theme",void 0);m([I()],W.prototype,"propertyValue",void 0);m([I()],W.prototype,"value",void 0);class Tt{get values(){return this._values}get rawValues(){return this._rawValues}constructor(e){if(this._values=[],this._rawValues={},e){const o=e.propertyName,i=e.presets??[];this._values=(i||[]).map(s=>s.startsWith("--")?`var(${s})`:s);const n=document.createElement("div");n.style.borderStyle="solid",n.style.visibility="hidden",document.body.append(n);try{this._values.forEach(s=>{n.style.setProperty(o,s);const r=getComputedStyle(n);this._rawValues[s]=r.getPropertyValue(o).trim()})}finally{n.remove()}}}tryMapToRawValue(e){return this._rawValues[e]??e}tryMapToPreset(e){return this.findPreset(e)??e}findPreset(e){const o=e&&e.trim();return this.values.find(i=>this._rawValues[i]===o)}}class Ri extends CustomEvent{constructor(e){super("change",{detail:{value:e}})}}let $t=class extends A{constructor(){super(...arguments),this.value="",this.showClearButton=!1}static get styles(){return $`
      :host {
        display: inline-block;
        width: 100%;
        position: relative;
      }

      input {
        width: 100%;
        box-sizing: border-box;
        padding: 0.25rem 0.375rem;
        color: inherit;
        background: rgba(0, 0, 0, 0.2);
        border-radius: 0.25rem;
        border: none;
      }

      button {
        display: none;
        position: absolute;
        right: 4px;
        top: 4px;
        padding: 0;
        line-height: 0;
        border: none;
        background: none;
        color: var(--dev-tools-text-color);
      }

      button svg {
        width: 16px;
        height: 16px;
      }

      button:not(:disabled):hover {
        color: var(--dev-tools-text-color-emphasis);
      }

      :host(.show-clear-button) input {
        padding-right: 20px;
      }

      :host(.show-clear-button) button {
        display: block;
      }
    `}update(t){super.update(t),t.has("showClearButton")&&(this.showClearButton?this.classList.add("show-clear-button"):this.classList.remove("show-clear-button"))}render(){return _`
      <input class="input" .value=${this.value} @change=${this.handleInputChange} />
      <button @click=${this.handleClearClick}>${_t.cross}</button>
    `}handleInputChange(t){const e=t.target;this.dispatchEvent(new Ri(e.value))}handleClearClick(){this.dispatchEvent(new Ri(""))}};m([w({})],$t.prototype,"value",void 0);m([w({})],$t.prototype,"showClearButton",void 0);$t=m([V("vaadin-dev-tools-theme-text-input")],$t);class Ar extends CustomEvent{constructor(e){super("class-name-change",{detail:{value:e}})}}let Qe=class extends A{constructor(){super(...arguments),this.editedClassName="",this.invalid=!1}static get styles(){return[fn,$`
        .editor-row {
          padding-top: 0;
        }

        .editor-row .editor .error {
          display: inline-block;
          color: var(--dev-tools-red-color);
          margin-top: 4px;
        }
      `]}update(t){super.update(t),t.has("className")&&(this.editedClassName=this.className,this.invalid=!1)}render(){return _` <div class="editor-row local-class-name">
      <div class="label">CSS class name</div>
      <div class="editor">
        <vaadin-dev-tools-theme-text-input
          type="text"
          .value=${this.editedClassName}
          @change=${this.handleInputChange}
        ></vaadin-dev-tools-theme-text-input>
        ${this.invalid?_`<br /><span class="error">Please enter a valid CSS class name</span>`:null}
      </div>
    </div>`}handleInputChange(t){this.editedClassName=t.detail.value;const e=/^-?[_a-zA-Z]+[_a-zA-Z0-9-]*$/;this.invalid=!this.editedClassName.match(e),!this.invalid&&this.editedClassName!==this.className&&this.dispatchEvent(new Ar(this.editedClassName))}};m([w({})],Qe.prototype,"className",void 0);m([I()],Qe.prototype,"editedClassName",void 0);m([I()],Qe.prototype,"invalid",void 0);Qe=m([V("vaadin-dev-tools-theme-class-name-editor")],Qe);class Rr extends CustomEvent{constructor(e){super("scope-change",{detail:{value:e}})}}Vo($`
  vaadin-select-overlay[theme~='vaadin-dev-tools-theme-scope-selector'] {
    --lumo-primary-color-50pct: rgba(255, 255, 255, 0.5);
    z-index: 100000 !important;
  }

  vaadin-select-overlay[theme~='vaadin-dev-tools-theme-scope-selector']::part(overlay) {
    background: #333;
  }

  vaadin-select-overlay[theme~='vaadin-dev-tools-theme-scope-selector'] vaadin-item {
    color: rgba(255, 255, 255, 0.8);
  }

  vaadin-select-overlay[theme~='vaadin-dev-tools-theme-scope-selector'] vaadin-item::part(content) {
    font-size: 13px;
  }

  vaadin-select-overlay[theme~='vaadin-dev-tools-theme-scope-selector'] vaadin-item .title {
    color: rgba(255, 255, 255, 0.95);
    font-weight: bold;
  }

  vaadin-select-overlay[theme~='vaadin-dev-tools-theme-scope-selector'] vaadin-item::part(checkmark) {
    margin: 6px;
  }

  vaadin-select-overlay[theme~='vaadin-dev-tools-theme-scope-selector'] vaadin-item::part(checkmark)::before {
    color: rgba(255, 255, 255, 0.95);
  }

  vaadin-select-overlay[theme~='vaadin-dev-tools-theme-scope-selector'] vaadin-item:hover {
    background: rgba(255, 255, 255, 0.1);
  }
`);let Ze=class extends A{constructor(){super(...arguments),this.value=P.local}static get styles(){return $`
      vaadin-select {
        --lumo-primary-color-50pct: rgba(255, 255, 255, 0.5);
        width: 100px;
      }

      vaadin-select::part(input-field) {
        background: rgba(0, 0, 0, 0.2);
      }

      vaadin-select vaadin-select-value-button,
      vaadin-select::part(toggle-button) {
        color: var(--dev-tools-text-color);
      }

      vaadin-select:hover vaadin-select-value-button,
      vaadin-select:hover::part(toggle-button) {
        color: var(--dev-tools-text-color-emphasis);
      }

      vaadin-select vaadin-select-item {
        font-size: 13px;
      }
    `}update(t){var e;super.update(t),t.has("metadata")&&((e=this.select)==null||e.requestContentUpdate())}render(){return _` <vaadin-select
      theme="small vaadin-dev-tools-theme-scope-selector"
      .value=${this.value}
      .renderer=${this.selectRenderer.bind(this)}
      @value-changed=${this.handleValueChange}
    ></vaadin-select>`}selectRenderer(t){var e;const o=((e=this.metadata)==null?void 0:e.displayName)||"Component",i=`${o}s`;we(_`
        <vaadin-list-box>
          <vaadin-item value=${P.local} label="Local">
            <span class="title">Local</span>
            <br />
            <span>Edit styles for this ${o}</span>
          </vaadin-item>
          <vaadin-item value=${P.global} label="Global">
            <span class="title">Global</span>
            <br />
            <span>Edit styles for all ${i}</span>
          </vaadin-item>
        </vaadin-list-box>
      `,t)}handleValueChange(t){const e=t.detail.value;e!==this.value&&this.dispatchEvent(new Rr(e))}};m([w({})],Ze.prototype,"value",void 0);m([w({})],Ze.prototype,"metadata",void 0);m([it("vaadin-select")],Ze.prototype,"select",void 0);Ze=m([V("vaadin-dev-tools-theme-scope-selector")],Ze);let Ni=class extends W{static get styles(){return[W.styles,$`
        .editor-row {
          align-items: center;
        }
      `]}handleInputChange(t){const e=t.target.checked?this.propertyMetadata.checkedValue:"";this.dispatchChange(e||"")}renderEditor(){const t=this.value===this.propertyMetadata.checkedValue;return _` <input type="checkbox" .checked=${t} @change=${this.handleInputChange} /> `}};Ni=m([V("vaadin-dev-tools-theme-checkbox-property-editor")],Ni);let Ii=class extends W{handleInputChange(t){this.dispatchChange(t.detail.value)}renderEditor(){var t;return _`
      <vaadin-dev-tools-theme-text-input
        .value=${this.value}
        .showClearButton=${((t=this.propertyValue)==null?void 0:t.modified)||!1}
        @change=${this.handleInputChange}
      ></vaadin-dev-tools-theme-text-input>
    `}};Ii=m([V("vaadin-dev-tools-theme-text-property-editor")],Ii);let At=class extends W{constructor(){super(...arguments),this.selectedPresetIndex=-1,this.presets=new Tt}static get styles(){return[W.styles,$`
        :host {
          --preset-count: 3;
          --slider-bg: #fff;
          --slider-border: #333;
        }

        .editor-row {
          align-items: center;
        }

        .editor-row > .editor {
          display: flex;
          align-items: center;
          gap: 1rem;
        }

        .editor-row .input {
          flex: 0 0 auto;
          width: 80px;
        }

        .slider-wrapper {
          flex: 1 1 0;
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }

        .icon {
          width: 20px;
          height: 20px;
          color: #aaa;
        }

        .icon.prefix > svg {
          transform: scale(0.75);
        }

        .slider {
          flex: 1 1 0;
          -webkit-appearance: none;
          background: linear-gradient(to right, #666, #666 2px, transparent 2px);
          background-size: calc((100% - 13px) / (var(--preset-count) - 1)) 8px;
          background-position: 5px 50%;
          background-repeat: repeat-x;
        }

        .slider::-webkit-slider-runnable-track {
          width: 100%;
          box-sizing: border-box;
          height: 16px;
          background-image: linear-gradient(#666, #666);
          background-size: calc(100% - 12px) 2px;
          background-repeat: no-repeat;
          background-position: 6px 50%;
        }

        .slider::-moz-range-track {
          width: 100%;
          box-sizing: border-box;
          height: 16px;
          background-image: linear-gradient(#666, #666);
          background-size: calc(100% - 12px) 2px;
          background-repeat: no-repeat;
          background-position: 6px 50%;
        }

        .slider::-webkit-slider-thumb {
          -webkit-appearance: none;
          height: 16px;
          width: 16px;
          border: 2px solid var(--slider-border);
          border-radius: 50%;
          background: var(--slider-bg);
          cursor: pointer;
        }

        .slider::-moz-range-thumb {
          height: 16px;
          width: 16px;
          border: 2px solid var(--slider-border);
          border-radius: 50%;
          background: var(--slider-bg);
          cursor: pointer;
        }

        .custom-value {
          opacity: 0.5;
        }

        .custom-value:hover,
        .custom-value:focus-within {
          opacity: 1;
        }

        .custom-value:not(:hover, :focus-within) {
          --slider-bg: #333;
          --slider-border: #666;
        }
      `]}update(t){t.has("propertyMetadata")&&(this.presets=new Tt(this.propertyMetadata)),super.update(t)}renderEditor(){var t;const e={"slider-wrapper":!0,"custom-value":this.selectedPresetIndex<0},o=this.presets.values.length;return _`
      <div class=${Mo(e)}>
        ${null}
        <input
          type="range"
          class="slider"
          style="--preset-count: ${o}"
          step="1"
          min="0"
          .max=${(o-1).toString()}
          .value=${this.selectedPresetIndex}
          @input=${this.handleSliderInput}
          @change=${this.handleSliderChange}
        />
        ${null}
      </div>
      <vaadin-dev-tools-theme-text-input
        class="input"
        .value=${this.value}
        .showClearButton=${((t=this.propertyValue)==null?void 0:t.modified)||!1}
        @change=${this.handleValueChange}
      ></vaadin-dev-tools-theme-text-input>
    `}handleSliderInput(t){const e=t.target,o=parseInt(e.value),i=this.presets.values[o];this.selectedPresetIndex=o,this.value=this.presets.rawValues[i]}handleSliderChange(){this.dispatchChange(this.value)}handleValueChange(t){this.value=t.detail.value,this.updateSliderValue(),this.dispatchChange(this.value)}dispatchChange(t){const e=this.presets.tryMapToPreset(t);super.dispatchChange(e)}updateValueFromTheme(){var t;super.updateValueFromTheme(),this.value=this.presets.tryMapToRawValue(((t=this.propertyValue)==null?void 0:t.value)||""),this.updateSliderValue()}updateSliderValue(){const t=this.presets.findPreset(this.value);this.selectedPresetIndex=t?this.presets.values.indexOf(t):-1}};m([I()],At.prototype,"selectedPresetIndex",void 0);m([I()],At.prototype,"presets",void 0);At=m([V("vaadin-dev-tools-theme-range-property-editor")],At);const ze=(t,e=0,o=1)=>t>o?o:t<e?e:t,U=(t,e=0,o=Math.pow(10,e))=>Math.round(o*t)/o,gn=({h:t,s:e,v:o,a:i})=>{const n=(200-e)*o/100;return{h:U(t),s:U(n>0&&n<200?e*o/100/(n<=100?n:200-n)*100:0),l:U(n/2),a:U(i,2)}},To=t=>{const{h:e,s:o,l:i}=gn(t);return`hsl(${e}, ${o}%, ${i}%)`},po=t=>{const{h:e,s:o,l:i,a:n}=gn(t);return`hsla(${e}, ${o}%, ${i}%, ${n})`},Nr=({h:t,s:e,v:o,a:i})=>{t=t/360*6,e=e/100,o=o/100;const n=Math.floor(t),s=o*(1-e),r=o*(1-(t-n)*e),l=o*(1-(1-t+n)*e),a=n%6;return{r:U([o,r,s,s,l,o][a]*255),g:U([l,o,o,r,s,s][a]*255),b:U([s,s,l,o,o,r][a]*255),a:U(i,2)}},Ir=t=>{const{r:e,g:o,b:i,a:n}=Nr(t);return`rgba(${e}, ${o}, ${i}, ${n})`},Pr=t=>{const e=/rgba?\(?\s*(-?\d*\.?\d+)(%)?[,\s]+(-?\d*\.?\d+)(%)?[,\s]+(-?\d*\.?\d+)(%)?,?\s*[/\s]*(-?\d*\.?\d+)?(%)?\s*\)?/i.exec(t);return e?Or({r:Number(e[1])/(e[2]?100/255:1),g:Number(e[3])/(e[4]?100/255:1),b:Number(e[5])/(e[6]?100/255:1),a:e[7]===void 0?1:Number(e[7])/(e[8]?100:1)}):{h:0,s:0,v:0,a:1}},Or=({r:t,g:e,b:o,a:i})=>{const n=Math.max(t,e,o),s=n-Math.min(t,e,o),r=s?n===t?(e-o)/s:n===e?2+(o-t)/s:4+(t-e)/s:0;return{h:U(60*(r<0?r+6:r)),s:U(n?s/n*100:0),v:U(n/255*100),a:i}},Lr=(t,e)=>{if(t===e)return!0;for(const o in t)if(t[o]!==e[o])return!1;return!0},Mr=(t,e)=>t.replace(/\s/g,"")===e.replace(/\s/g,""),Pi={},yn=t=>{let e=Pi[t];return e||(e=document.createElement("template"),e.innerHTML=t,Pi[t]=e),e},Do=(t,e,o)=>{t.dispatchEvent(new CustomEvent(e,{bubbles:!0,detail:o}))};let Oe=!1;const $o=t=>"touches"in t,Vr=t=>Oe&&!$o(t)?!1:(Oe||(Oe=$o(t)),!0),Oi=(t,e)=>{const o=$o(e)?e.touches[0]:e,i=t.el.getBoundingClientRect();Do(t.el,"move",t.getMove({x:ze((o.pageX-(i.left+window.pageXOffset))/i.width),y:ze((o.pageY-(i.top+window.pageYOffset))/i.height)}))},Dr=(t,e)=>{const o=e.keyCode;o>40||t.xy&&o<37||o<33||(e.preventDefault(),Do(t.el,"move",t.getMove({x:o===39?.01:o===37?-.01:o===34?.05:o===33?-.05:o===35?1:o===36?-1:0,y:o===40?.01:o===38?-.01:0},!0)))};class Uo{constructor(e,o,i,n){const s=yn(`<div role="slider" tabindex="0" part="${o}" ${i}><div part="${o}-pointer"></div></div>`);e.appendChild(s.content.cloneNode(!0));const r=e.querySelector(`[part=${o}]`);r.addEventListener("mousedown",this),r.addEventListener("touchstart",this),r.addEventListener("keydown",this),this.el=r,this.xy=n,this.nodes=[r.firstChild,r]}set dragging(e){const o=e?document.addEventListener:document.removeEventListener;o(Oe?"touchmove":"mousemove",this),o(Oe?"touchend":"mouseup",this)}handleEvent(e){switch(e.type){case"mousedown":case"touchstart":if(e.preventDefault(),!Vr(e)||!Oe&&e.button!=0)return;this.el.focus(),Oi(this,e),this.dragging=!0;break;case"mousemove":case"touchmove":e.preventDefault(),Oi(this,e);break;case"mouseup":case"touchend":this.dragging=!1;break;case"keydown":Dr(this,e);break}}style(e){e.forEach((o,i)=>{for(const n in o)this.nodes[i].style.setProperty(n,o[n])})}}class Ur extends Uo{constructor(e){super(e,"hue",'aria-label="Hue" aria-valuemin="0" aria-valuemax="360"',!1)}update({h:e}){this.h=e,this.style([{left:`${e/360*100}%`,color:To({h:e,s:100,v:100,a:1})}]),this.el.setAttribute("aria-valuenow",`${U(e)}`)}getMove(e,o){return{h:o?ze(this.h+e.x*360,0,360):360*e.x}}}class zr extends Uo{constructor(e){super(e,"saturation",'aria-label="Color"',!0)}update(e){this.hsva=e,this.style([{top:`${100-e.v}%`,left:`${e.s}%`,color:To(e)},{"background-color":To({h:e.h,s:100,v:100,a:1})}]),this.el.setAttribute("aria-valuetext",`Saturation ${U(e.s)}%, Brightness ${U(e.v)}%`)}getMove(e,o){return{s:o?ze(this.hsva.s+e.x*100,0,100):e.x*100,v:o?ze(this.hsva.v-e.y*100,0,100):Math.round(100-e.y*100)}}}const jr=':host{display:flex;flex-direction:column;position:relative;width:200px;height:200px;user-select:none;-webkit-user-select:none;cursor:default}:host([hidden]){display:none!important}[role=slider]{position:relative;touch-action:none;user-select:none;-webkit-user-select:none;outline:0}[role=slider]:last-child{border-radius:0 0 8px 8px}[part$=pointer]{position:absolute;z-index:1;box-sizing:border-box;width:28px;height:28px;display:flex;place-content:center center;transform:translate(-50%,-50%);background-color:#fff;border:2px solid #fff;border-radius:50%;box-shadow:0 2px 4px rgba(0,0,0,.2)}[part$=pointer]::after{content:"";width:100%;height:100%;border-radius:inherit;background-color:currentColor}[role=slider]:focus [part$=pointer]{transform:translate(-50%,-50%) scale(1.1)}',Fr="[part=hue]{flex:0 0 24px;background:linear-gradient(to right,red 0,#ff0 17%,#0f0 33%,#0ff 50%,#00f 67%,#f0f 83%,red 100%)}[part=hue-pointer]{top:50%;z-index:2}",Br="[part=saturation]{flex-grow:1;border-color:transparent;border-bottom:12px solid #000;border-radius:8px 8px 0 0;background-image:linear-gradient(to top,#000,transparent),linear-gradient(to right,#fff,rgba(255,255,255,0));box-shadow:inset 0 0 0 1px rgba(0,0,0,.05)}[part=saturation-pointer]{z-index:3}",pt=Symbol("same"),mo=Symbol("color"),Li=Symbol("hsva"),vo=Symbol("update"),Mi=Symbol("parts"),Rt=Symbol("css"),Nt=Symbol("sliders");let Hr=class extends HTMLElement{static get observedAttributes(){return["color"]}get[Rt](){return[jr,Fr,Br]}get[Nt](){return[zr,Ur]}get color(){return this[mo]}set color(t){if(!this[pt](t)){const e=this.colorModel.toHsva(t);this[vo](e),this[mo]=t}}constructor(){super();const t=yn(`<style>${this[Rt].join("")}</style>`),e=this.attachShadow({mode:"open"});e.appendChild(t.content.cloneNode(!0)),e.addEventListener("move",this),this[Mi]=this[Nt].map(o=>new o(e))}connectedCallback(){if(this.hasOwnProperty("color")){const t=this.color;delete this.color,this.color=t}else this.color||(this.color=this.colorModel.defaultColor)}attributeChangedCallback(t,e,o){const i=this.colorModel.fromAttr(o);this[pt](i)||(this.color=i)}handleEvent(t){const e=this[Li],o={...e,...t.detail};this[vo](o);let i;!Lr(o,e)&&!this[pt](i=this.colorModel.fromHsva(o))&&(this[mo]=i,Do(this,"color-changed",{value:i}))}[pt](t){return this.color&&this.colorModel.equal(t,this.color)}[vo](t){this[Li]=t,this[Mi].forEach(e=>e.update(t))}};class qr extends Uo{constructor(e){super(e,"alpha",'aria-label="Alpha" aria-valuemin="0" aria-valuemax="1"',!1)}update(e){this.hsva=e;const o=po({...e,a:0}),i=po({...e,a:1}),n=e.a*100;this.style([{left:`${n}%`,color:po(e)},{"--gradient":`linear-gradient(90deg, ${o}, ${i}`}]);const s=U(n);this.el.setAttribute("aria-valuenow",`${s}`),this.el.setAttribute("aria-valuetext",`${s}%`)}getMove(e,o){return{a:o?ze(this.hsva.a+e.x):e.x}}}const Wr=`[part=alpha]{flex:0 0 24px}[part=alpha]::after{display:block;content:"";position:absolute;top:0;left:0;right:0;bottom:0;border-radius:inherit;background-image:var(--gradient);box-shadow:inset 0 0 0 1px rgba(0,0,0,.05)}[part^=alpha]{background-color:#fff;background-image:url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill-opacity=".05"><rect x="8" width="8" height="8"/><rect y="8" width="8" height="8"/></svg>')}[part=alpha-pointer]{top:50%}`;class Gr extends Hr{get[Rt](){return[...super[Rt],Wr]}get[Nt](){return[...super[Nt],qr]}}const Kr={defaultColor:"rgba(0, 0, 0, 1)",toHsva:Pr,fromHsva:Ir,equal:Mr,fromAttr:t=>t};class Yr extends Gr{get colorModel(){return Kr}}/**
* @license
* Copyright (c) 2017 - 2023 Vaadin Ltd.
* This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
*/function Jr(t){const e=[];for(;t;){if(t.nodeType===Node.DOCUMENT_NODE){e.push(t);break}if(t.nodeType===Node.DOCUMENT_FRAGMENT_NODE){e.push(t),t=t.host;continue}if(t.assignedSlot){t=t.assignedSlot;continue}t=t.parentNode}return e}const fo={start:"top",end:"bottom"},go={start:"left",end:"right"},Vi=new ResizeObserver(t=>{setTimeout(()=>{t.forEach(e=>{e.target.__overlay&&e.target.__overlay._updatePosition()})})}),Xr=t=>class extends t{static get properties(){return{positionTarget:{type:Object,value:null},horizontalAlign:{type:String,value:"start"},verticalAlign:{type:String,value:"top"},noHorizontalOverlap:{type:Boolean,value:!1},noVerticalOverlap:{type:Boolean,value:!1},requiredVerticalSpace:{type:Number,value:0}}}static get observers(){return["__positionSettingsChanged(horizontalAlign, verticalAlign, noHorizontalOverlap, noVerticalOverlap, requiredVerticalSpace)","__overlayOpenedChanged(opened, positionTarget)"]}constructor(){super(),this.__onScroll=this.__onScroll.bind(this),this._updatePosition=this._updatePosition.bind(this)}connectedCallback(){super.connectedCallback(),this.opened&&this.__addUpdatePositionEventListeners()}disconnectedCallback(){super.disconnectedCallback(),this.__removeUpdatePositionEventListeners()}__addUpdatePositionEventListeners(){window.addEventListener("resize",this._updatePosition),this.__positionTargetAncestorRootNodes=Jr(this.positionTarget),this.__positionTargetAncestorRootNodes.forEach(e=>{e.addEventListener("scroll",this.__onScroll,!0)})}__removeUpdatePositionEventListeners(){window.removeEventListener("resize",this._updatePosition),this.__positionTargetAncestorRootNodes&&(this.__positionTargetAncestorRootNodes.forEach(e=>{e.removeEventListener("scroll",this.__onScroll,!0)}),this.__positionTargetAncestorRootNodes=null)}__overlayOpenedChanged(e,o){if(this.__removeUpdatePositionEventListeners(),o&&(o.__overlay=null,Vi.unobserve(o),e&&(this.__addUpdatePositionEventListeners(),o.__overlay=this,Vi.observe(o))),e){const i=getComputedStyle(this);this.__margins||(this.__margins={},["top","bottom","left","right"].forEach(n=>{this.__margins[n]=parseInt(i[n],10)})),this.setAttribute("dir",i.direction),this._updatePosition(),requestAnimationFrame(()=>this._updatePosition())}}__positionSettingsChanged(){this._updatePosition()}__onScroll(e){this.contains(e.target)||this._updatePosition()}_updatePosition(){if(!this.positionTarget||!this.opened)return;const e=this.positionTarget.getBoundingClientRect(),o=this.__shouldAlignStartVertically(e);this.style.justifyContent=o?"flex-start":"flex-end";const i=this.__isRTL,n=this.__shouldAlignStartHorizontally(e,i),s=!i&&n||i&&!n;this.style.alignItems=s?"flex-start":"flex-end";const r=this.getBoundingClientRect(),l=this.__calculatePositionInOneDimension(e,r,this.noVerticalOverlap,fo,this,o),a=this.__calculatePositionInOneDimension(e,r,this.noHorizontalOverlap,go,this,n);Object.assign(this.style,l,a),this.toggleAttribute("bottom-aligned",!o),this.toggleAttribute("top-aligned",o),this.toggleAttribute("end-aligned",!s),this.toggleAttribute("start-aligned",s)}__shouldAlignStartHorizontally(e,o){const i=Math.max(this.__oldContentWidth||0,this.$.overlay.offsetWidth);this.__oldContentWidth=this.$.overlay.offsetWidth;const n=Math.min(window.innerWidth,document.documentElement.clientWidth),s=!o&&this.horizontalAlign==="start"||o&&this.horizontalAlign==="end";return this.__shouldAlignStart(e,i,n,this.__margins,s,this.noHorizontalOverlap,go)}__shouldAlignStartVertically(e){const o=this.requiredVerticalSpace||Math.max(this.__oldContentHeight||0,this.$.overlay.offsetHeight);this.__oldContentHeight=this.$.overlay.offsetHeight;const i=Math.min(window.innerHeight,document.documentElement.clientHeight),n=this.verticalAlign==="top";return this.__shouldAlignStart(e,o,i,this.__margins,n,this.noVerticalOverlap,fo)}__shouldAlignStart(e,o,i,n,s,r,l){const a=i-e[r?l.end:l.start]-n[l.end],d=e[r?l.start:l.end]-n[l.start],h=s?a:d,v=h>(s?d:a)||h>o;return s===v}__adjustBottomProperty(e,o,i){let n;if(e===o.end){if(o.end===fo.end){const s=Math.min(window.innerHeight,document.documentElement.clientHeight);if(i>s&&this.__oldViewportHeight){const r=this.__oldViewportHeight-s;n=i-r}this.__oldViewportHeight=s}if(o.end===go.end){const s=Math.min(window.innerWidth,document.documentElement.clientWidth);if(i>s&&this.__oldViewportWidth){const r=this.__oldViewportWidth-s;n=i-r}this.__oldViewportWidth=s}}return n}__calculatePositionInOneDimension(e,o,i,n,s,r){const l=r?n.start:n.end,a=r?n.end:n.start,d=parseFloat(s.style[l]||getComputedStyle(s)[l]),h=this.__adjustBottomProperty(l,n,d),v=o[r?n.start:n.end]-e[i===r?n.end:n.start],u=h?`${h}px`:`${d+v*(r?-1:1)}px`;return{[l]:u,[a]:""}}};class Qr extends CustomEvent{constructor(e){super("color-picker-change",{detail:{value:e}})}}const _n=$`
  :host {
    --preview-size: 24px;
    --preview-color: rgba(0, 0, 0, 0);
  }

  .preview {
    --preview-bg-size: calc(var(--preview-size) / 2);
    --preview-bg-pos: calc(var(--preview-size) / 4);

    width: var(--preview-size);
    height: var(--preview-size);
    padding: 0;
    position: relative;
    overflow: hidden;
    background: none;
    border: solid 2px #888;
    border-radius: 4px;
    box-sizing: content-box;
  }

  .preview::before,
  .preview::after {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }

  .preview::before {
    content: '';
    background: white;
    background-image: linear-gradient(45deg, #666 25%, transparent 25%),
      linear-gradient(45deg, transparent 75%, #666 75%), linear-gradient(45deg, transparent 75%, #666 75%),
      linear-gradient(45deg, #666 25%, transparent 25%);
    background-size: var(--preview-bg-size) var(--preview-bg-size);
    background-position: 0 0, 0 0, calc(var(--preview-bg-pos) * -1) calc(var(--preview-bg-pos) * -1),
      var(--preview-bg-pos) var(--preview-bg-pos);
  }

  .preview::after {
    content: '';
    background-color: var(--preview-color);
  }
`;let et=class extends A{constructor(){super(...arguments),this.commitValue=!1}static get styles(){return[_n,$`
        #toggle {
          display: block;
        }
      `]}update(t){super.update(t),t.has("value")&&this.overlay&&this.overlay.requestContentUpdate()}firstUpdated(){this.overlay=document.createElement("vaadin-dev-tools-color-picker-overlay"),this.overlay.renderer=this.renderOverlayContent.bind(this),this.overlay.owner=this,this.overlay.positionTarget=this.toggle,this.overlay.noVerticalOverlap=!0,this.overlay.addEventListener("vaadin-overlay-escape-press",this.handleOverlayEscape.bind(this)),this.overlay.addEventListener("vaadin-overlay-close",this.handleOverlayClose.bind(this)),this.append(this.overlay)}render(){const t=this.value||"rgba(0, 0, 0, 0)";return _` <button
      id="toggle"
      class="preview"
      style="--preview-color: ${t}"
      @click=${this.open}
    ></button>`}open(){this.commitValue=!1,this.overlay.opened=!0,this.overlay.style.zIndex="1000000";const t=this.overlay.shadowRoot.querySelector('[part="overlay"]');t.style.background="#333"}renderOverlayContent(t){const e=getComputedStyle(this.toggle,"::after").getPropertyValue("background-color");we(_` <div>
        <vaadin-dev-tools-color-picker-overlay-content
          .value=${e}
          .presets=${this.presets}
          @color-changed=${this.handleColorChange.bind(this)}
        ></vaadin-dev-tools-color-picker-overlay-content>
      </div>`,t)}handleColorChange(t){this.commitValue=!0,this.dispatchEvent(new Qr(t.detail.value)),t.detail.close&&(this.overlay.opened=!1,this.handleOverlayClose())}handleOverlayEscape(){this.commitValue=!1}handleOverlayClose(){const t=this.commitValue?"color-picker-commit":"color-picker-cancel";this.dispatchEvent(new CustomEvent(t))}};m([w({})],et.prototype,"value",void 0);m([w({})],et.prototype,"presets",void 0);m([it("#toggle")],et.prototype,"toggle",void 0);et=m([V("vaadin-dev-tools-color-picker")],et);let It=class extends A{static get styles(){return[_n,$`
        :host {
          display: block;
          padding: 12px;
        }

        .picker::part(saturation),
        .picker::part(hue) {
          margin-bottom: 10px;
        }

        .picker::part(hue),
        .picker::part(alpha) {
          flex: 0 0 20px;
        }

        .picker::part(saturation),
        .picker::part(hue),
        .picker::part(alpha) {
          border-radius: 3px;
        }

        .picker::part(saturation-pointer),
        .picker::part(hue-pointer),
        .picker::part(alpha-pointer) {
          width: 20px;
          height: 20px;
        }

        .swatches {
          display: grid;
          grid-template-columns: repeat(6, var(--preview-size));
          grid-column-gap: 10px;
          grid-row-gap: 6px;
          margin-top: 16px;
        }
      `]}render(){return _` <div>
      <vaadin-dev-tools-rgba-string-color-picker
        class="picker"
        .color=${this.value}
        @color-changed=${this.handlePickerChange}
      ></vaadin-dev-tools-rgba-string-color-picker>
      ${this.renderSwatches()}
    </div>`}renderSwatches(){if(!this.presets||this.presets.length===0)return;const t=this.presets.map(e=>_` <button
        class="preview"
        style="--preview-color: ${e}"
        @click=${()=>this.selectPreset(e)}
      ></button>`);return _` <div class="swatches">${t}</div>`}handlePickerChange(t){this.dispatchEvent(new CustomEvent("color-changed",{detail:{value:t.detail.value}}))}selectPreset(t){this.dispatchEvent(new CustomEvent("color-changed",{detail:{value:t,close:!0}}))}};m([w({})],It.prototype,"value",void 0);m([w({})],It.prototype,"presets",void 0);It=m([V("vaadin-dev-tools-color-picker-overlay-content")],It);customElements.whenDefined("vaadin-overlay").then(()=>{const t=customElements.get("vaadin-overlay");class e extends Xr(t){}customElements.define("vaadin-dev-tools-color-picker-overlay",e)});customElements.define("vaadin-dev-tools-rgba-string-color-picker",Yr);let Di=class extends W{constructor(){super(...arguments),this.presets=new Tt}static get styles(){return[W.styles,$`
        .editor-row {
          align-items: center;
        }

        .editor-row > .editor {
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }
      `]}update(t){t.has("propertyMetadata")&&(this.presets=new Tt(this.propertyMetadata)),super.update(t)}renderEditor(){var t;return _`
      <vaadin-dev-tools-color-picker
        .value=${this.value}
        .presets=${this.presets.values}
        @color-picker-change=${this.handleColorPickerChange}
        @color-picker-commit=${this.handleColorPickerCommit}
        @color-picker-cancel=${this.handleColorPickerCancel}
      ></vaadin-dev-tools-color-picker>
      <vaadin-dev-tools-theme-text-input
        .value=${this.value}
        .showClearButton=${((t=this.propertyValue)==null?void 0:t.modified)||!1}
        @change=${this.handleInputChange}
      ></vaadin-dev-tools-theme-text-input>
    `}handleInputChange(t){this.value=t.detail.value,this.dispatchChange(this.value)}handleColorPickerChange(t){this.value=t.detail.value}handleColorPickerCommit(){this.dispatchChange(this.value)}handleColorPickerCancel(){this.updateValueFromTheme()}dispatchChange(t){const e=this.presets.tryMapToPreset(t);super.dispatchChange(e)}updateValueFromTheme(){var t;super.updateValueFromTheme(),this.value=this.presets.tryMapToRawValue(((t=this.propertyValue)==null?void 0:t.value)||"")}};Di=m([V("vaadin-dev-tools-theme-color-property-editor")],Di);class Zr extends CustomEvent{constructor(e){super("open-css",{detail:{element:e}})}}let Pt=class extends A{static get styles(){return $`
      .section .header {
        display: flex;
        align-items: baseline;
        justify-content: space-between;
        padding: 0.4rem var(--theme-editor-section-horizontal-padding);
        color: var(--dev-tools-text-color-emphasis);
        background-color: rgba(0, 0, 0, 0.2);
      }

      .section .property-list .property-editor:not(:last-child) {
        border-bottom: solid 1px rgba(0, 0, 0, 0.2);
      }

      .section .header .open-css {
        all: initial;
        font-family: inherit;
        font-size: var(--dev-tools-font-size-small);
        line-height: 1;
        white-space: nowrap;
        background-color: rgba(255, 255, 255, 0.12);
        color: var(--dev-tools-text-color);
        font-weight: 600;
        padding: 0.25rem 0.375rem;
        border-radius: 0.25rem;
      }

      .section .header .open-css:hover {
        color: var(--dev-tools-text-color-emphasis);
      }
    `}render(){const t=this.metadata.elements.map(e=>this.renderSection(e));return _` <div>${t}</div> `}renderSection(t){const e=t.properties.map(o=>this.renderPropertyEditor(t,o));return _`
      <div class="section" data-testid=${t==null?void 0:t.displayName}>
        <div class="header">
          <span> ${t.displayName} </span>
          <button class="open-css" @click=${()=>this.handleOpenCss(t)}>Edit CSS</button>
        </div>
        <div class="property-list">${e}</div>
      </div>
    `}handleOpenCss(t){this.dispatchEvent(new Zr(t))}renderPropertyEditor(t,e){let o;switch(e.editorType){case N.checkbox:o=ht`vaadin-dev-tools-theme-checkbox-property-editor`;break;case N.range:o=ht`vaadin-dev-tools-theme-range-property-editor`;break;case N.color:o=ht`vaadin-dev-tools-theme-color-property-editor`;break;default:o=ht`vaadin-dev-tools-theme-text-property-editor`}return sr` <${o}
          class="property-editor"
          .elementMetadata=${t}
          .propertyMetadata=${e}
          .theme=${this.theme}
          data-testid=${e.propertyName}
        >
        </${o}>`}};m([w({})],Pt.prototype,"metadata",void 0);m([w({})],Pt.prototype,"theme",void 0);Pt=m([V("vaadin-dev-tools-theme-property-list")],Pt);let Ot=class extends A{render(){return _`<div
      tabindex="-1"
      @mousemove=${this.onMouseMove}
      @click=${this.onClick}
      @keydown=${this.onKeyDown}
    ></div>`}onClick(t){const e=this.getTargetElement(t);this.dispatchEvent(new CustomEvent("shim-click",{detail:{target:e}}))}onMouseMove(t){const e=this.getTargetElement(t);this.dispatchEvent(new CustomEvent("shim-mousemove",{detail:{target:e}}))}onKeyDown(t){this.dispatchEvent(new CustomEvent("shim-keydown",{detail:{originalEvent:t}}))}getTargetElement(t){this.style.display="none";const e=document.elementFromPoint(t.clientX,t.clientY);return this.style.display="",e}};Ot.shadowRootOptions={...A.shadowRootOptions,delegatesFocus:!0};Ot.styles=[$`
      div {
        pointer-events: auto;
        background: rgba(255, 255, 255, 0);
        position: fixed;
        inset: 0px;
        z-index: 1000000;
      }
    `];Ot=m([V("vaadin-dev-tools-shim")],Ot);const bn=$`
  .popup {
    width: auto;
    position: fixed;
    background-color: var(--dev-tools-background-color-active-blurred);
    color: var(--dev-tools-text-color-primary);
    padding: 0.1875rem 0.75rem 0.1875rem 1rem;
    background-clip: padding-box;
    border-radius: var(--dev-tools-border-radius);
    overflow: hidden;
    margin: 0.5rem;
    width: 30rem;
    max-width: calc(100% - 1rem);
    max-height: calc(100vh - 1rem);
    flex-shrink: 1;
    background-color: var(--dev-tools-background-color-active);
    color: var(--dev-tools-text-color);
    transition: var(--dev-tools-transition-duration);
    transform-origin: bottom right;
    display: flex;
    flex-direction: column;
    box-shadow: var(--dev-tools-box-shadow);
    outline: none;
  }
`,ea={resolve:t=>de(e=>e.classList.contains("cc-banner"),t)?document.querySelector("vaadin-cookie-consent"):void 0},ta={resolve:t=>{const e=de(o=>o.localName==="vaadin-login-overlay-wrapper",t);return e?e.__dataHost:void 0}},oa={resolve:t=>t.localName==="vaadin-dialog-overlay"?t.__dataHost:void 0},ia={resolve:t=>{const e=de(o=>o.localName==="vaadin-confirm-dialog-overlay",t);return e?e.__dataHost:void 0}},na={resolve:t=>{const e=de(o=>o.localName==="vaadin-notification-card",t);return e?e.__dataHost:void 0}},sa={resolve:t=>t.localName!=="vaadin-menu-bar-item"?void 0:de(e=>e.localName==="vaadin-menu-bar",t)},Ui=[ea,ta,oa,ia,na,sa],ra={resolve:t=>de(e=>e.classList.contains("cc-banner"),t)},aa={resolve:t=>{var e;const o=de(i=>{var n;return((n=i.shadowRoot)==null?void 0:n.querySelector("[part=overlay]"))!=null},t);return(e=o==null?void 0:o.shadowRoot)==null?void 0:e.querySelector("[part=overlay]")}},la={resolve:t=>{var e;const o=de(i=>i.localName==="vaadin-login-overlay-wrapper",t);return(e=o==null?void 0:o.shadowRoot)==null?void 0:e.querySelector("[part=card]")}},zi=[la,ra,aa],de=function(t,e){return t(e)?e:e.parentNode&&e.parentNode instanceof HTMLElement?de(t,e.parentNode):void 0};class da{resolveElement(e){for(const o in Ui){let i=e;if((i=Ui[o].resolve(e))!==void 0)return i}return e}}class ca{resolveElement(e){for(const o in zi){let i=e;if((i=zi[o].resolve(e))!==void 0)return i}return e}}const ha=new da,ua=new ca;let fe=class extends A{constructor(){super(),this.active=!1,this.components=[],this.selected=0,this.mouseMoveEvent=this.mouseMoveEvent.bind(this)}connectedCallback(){super.connectedCallback();const t=new CSSStyleSheet;t.replaceSync(`
    .vaadin-dev-tools-highlight-overlay {
      pointer-events: none;
      position: absolute;
      z-index: 10000;
      background: rgba(158,44,198,0.25);
    }`),document.adoptedStyleSheets=[...document.adoptedStyleSheets,t],this.overlayElement=document.createElement("div"),this.overlayElement.classList.add("vaadin-dev-tools-highlight-overlay"),this.addEventListener("mousemove",this.mouseMoveEvent)}disconnectedCallback(){super.disconnectedCallback(),this.removeEventListener("mousemove",this.mouseMoveEvent)}render(){var t;return this.active?(this.style.display="block",_`
      <vaadin-dev-tools-shim
        @shim-click=${this.shimClick}
        @shim-mousemove=${this.shimMove}
        @shim-keydown=${this.shimKeydown}
      ></vaadin-dev-tools-shim>
      <div class="window popup component-picker-info">${(t=this.options)==null?void 0:t.infoTemplate}</div>
      <div class="window popup component-picker-components-info">
        <div>
          ${this.components.map((e,o)=>_`<div class=${o===this.selected?"selected":""}>
                ${e.element.tagName.toLowerCase()}
              </div>`)}
        </div>
      </div>
    `):(this.style.display="none",null)}open(t){this.options=t,this.active=!0,this.dispatchEvent(new CustomEvent("component-picker-opened",{}))}close(){this.active=!1,this.dispatchEvent(new CustomEvent("component-picker-closed",{}))}update(t){if(super.update(t),(t.has("selected")||t.has("components"))&&this.highlight(this.components[this.selected]),t.has("active")){const e=t.get("active"),o=this.active;!e&&o?requestAnimationFrame(()=>this.shim.focus()):e&&!o&&this.highlight(void 0)}}mouseMoveEvent(t){var e;if(!this.active){this.style.display="none";return}const o=(e=this.shadowRoot)==null?void 0:e.querySelector(".component-picker-info");if(o){const i=o.getBoundingClientRect();t.x>i.x&&t.x<i.x+i.width&&t.y>i.y&&t.y<=i.y+i.height?o.style.opacity="0.05":o.style.opacity="1.0"}}shimKeydown(t){const e=t.detail.originalEvent;if(e.key==="Escape")this.close(),t.stopPropagation(),t.preventDefault();else if(e.key==="ArrowUp"){let o=this.selected-1;o<0&&(o=this.components.length-1),this.selected=o}else e.key==="ArrowDown"?this.selected=(this.selected+1)%this.components.length:e.key==="Enter"&&(this.pickSelectedComponent(),t.stopPropagation(),t.preventDefault())}shimMove(t){const e=ha.resolveElement(t.detail.target);this.components=lr(e),this.selected=this.components.length-1,this.components[this.selected].highlightElement=ua.resolveElement(t.detail.target)}shimClick(t){this.pickSelectedComponent()}pickSelectedComponent(){const t=this.components[this.selected];if(t&&this.options)try{this.options.pickCallback(t)}catch(e){console.error("Pick callback failed",e)}this.close()}highlight(t){let e=(t==null?void 0:t.highlightElement)??(t==null?void 0:t.element);if(this.highlighted!==e)if(e){const o=e.getBoundingClientRect(),i=getComputedStyle(e);this.overlayElement.style.top=`${o.top}px`,this.overlayElement.style.left=`${o.left}px`,this.overlayElement.style.width=`${o.width}px`,this.overlayElement.style.height=`${o.height}px`,this.overlayElement.style.borderRadius=i.borderRadius,document.body.append(this.overlayElement)}else this.overlayElement.remove();this.highlighted=e}};fe.styles=[bn,$`
      .component-picker-info {
        left: 1em;
        bottom: 1em;
      }

      .component-picker-components-info {
        right: 3em;
        bottom: 1em;
      }

      .component-picker-components-info .selected {
        font-weight: bold;
      }
    `];m([I()],fe.prototype,"active",void 0);m([I()],fe.prototype,"components",void 0);m([I()],fe.prototype,"selected",void 0);m([it("vaadin-dev-tools-shim")],fe.prototype,"shim",void 0);fe=m([V("vaadin-dev-tools-component-picker")],fe);const pa=Object.freeze(Object.defineProperty({__proto__:null,get ComponentPicker(){return fe}},Symbol.toStringTag,{value:"Module"}));class ma{constructor(){this.currentActiveComponent=null,this.currentActiveComponentMetaData=null,this.componentPicked=async(e,o)=>{await this.hideOverlay(),this.currentActiveComponent=e,this.currentActiveComponentMetaData=o},this.showOverlay=()=>{!this.currentActiveComponent||!this.currentActiveComponentMetaData||this.currentActiveComponentMetaData.openOverlay&&this.currentActiveComponentMetaData.openOverlay(this.currentActiveComponent)},this.hideOverlay=()=>{!this.currentActiveComponent||!this.currentActiveComponentMetaData||this.currentActiveComponentMetaData.hideOverlay&&this.currentActiveComponentMetaData.hideOverlay(this.currentActiveComponent)},this.reset=()=>{this.currentActiveComponent=null,this.currentActiveComponentMetaData=null}}}const _e=new ma,Ta=t=>{const e=t.element.$.comboBox,o=e.$.overlay;va(t.element,e,o)},$a=t=>{const e=t.element,o=e.$.comboBox,i=o.$.overlay;fa(e,o,i)},va=(t,e,o)=>{t.opened=!0,o._storedModeless=o.modeless,o.modeless=!0,document._themeEditorDocClickListener=ga(t,e),document.addEventListener("click",document._themeEditorDocClickListener),e.removeEventListener("focusout",e._boundOnFocusout)},fa=(t,e,o)=>{t.opened=!1,!(!e||!o)&&(o.modeless=o._storedModeless,delete o._storedModeless,e.addEventListener("focusout",e._boundOnFocusout),document.removeEventListener("click",document._themeEditorDocClickListener),delete document._themeEditorDocClickListener)},ga=(t,e)=>o=>{const i=o.target;i!=null&&(e.opened=!ya(i,t))};function ya(t,e){if(!t||!t.tagName)return!0;if(t.tagName.startsWith("VAADIN-DEV"))return!1;let o=t,i={nodeId:-1,uiId:-1,element:void 0};for(;o&&o.parentNode&&(i=xo(o),i.nodeId===-1);)o=o.parentElement?o.parentElement:o.parentNode.host;const n=xo(e);return!(i.nodeId!==-1&&n.nodeId===i.nodeId)}Vo($`
  .vaadin-theme-editor-highlight {
    outline: solid 2px #9e2cc6;
    outline-offset: 3px;
  }
`);let le=class extends A{constructor(){super(...arguments),this.expanded=!1,this.themeEditorState=De.enabled,this.context=null,this.baseTheme=null,this.editedTheme=null,this.effectiveTheme=null,this.markedAsUsed=!1}static get styles(){return $`
      :host {
        animation: fade-in var(--dev-tools-transition-duration) ease-in;
        --theme-editor-section-horizontal-padding: 0.75rem;
        display: flex;
        flex-direction: column;
        max-height: 400px;
      }

      .notice {
        padding: var(--theme-editor-section-horizontal-padding);
      }

      .notice a {
        color: var(--dev-tools-text-color-emphasis);
      }

      .hint vaadin-icon {
        color: var(--dev-tools-green-color);
        font-size: var(--lumo-icon-size-m);
      }

      .hint {
        display: flex;
        align-items: center;
        gap: var(--theme-editor-section-horizontal-padding);
      }

      .header {
        flex: 0 0 auto;
        border-bottom: solid 1px rgba(0, 0, 0, 0.2);
      }

      .header .picker-row {
        padding: var(--theme-editor-section-horizontal-padding);
        display: flex;
        gap: 20px;
        align-items: center;
        justify-content: space-between;
      }

      .picker {
        flex: 1 1 0;
        min-width: 0;
        display: flex;
        align-items: center;
      }

      .picker button {
        min-width: 0;
        display: inline-flex;
        align-items: center;
        padding: 0;
        line-height: 20px;
        border: none;
        background: none;
        color: var(--dev-tools-text-color);
      }

      .picker button:not(:disabled):hover {
        color: var(--dev-tools-text-color-emphasis);
      }

      .picker svg,
      .picker .component-type {
        flex: 0 0 auto;
        margin-right: 4px;
      }

      .picker .instance-name {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        color: #e5a2fce5;
      }

      .picker .instance-name-quote {
        color: #e5a2fce5;
      }

      .picker .no-selection {
        font-style: italic;
      }

      .actions {
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .property-list {
        flex: 1 1 auto;
        overflow-y: auto;
      }

      .link-button {
        all: initial;
        font-family: inherit;
        font-size: var(--dev-tools-font-size-small);
        line-height: 1;
        white-space: nowrap;
        color: inherit;
        font-weight: 600;
        text-decoration: underline;
      }

      .link-button:focus,
      .link-button:hover {
        color: var(--dev-tools-text-color-emphasis);
      }

      .icon-button {
        padding: 0;
        line-height: 0;
        border: none;
        background: none;
        color: var(--dev-tools-text-color);
      }

      .icon-button:disabled {
        opacity: 0.5;
      }

      .icon-button:not(:disabled):hover {
        color: var(--dev-tools-text-color-emphasis);
      }
    `}firstUpdated(){this.api=new kr(this.connection),this.history=new Tr(this.api),this.historyActions=this.history.allowedActions,this.undoRedoListener=t=>{var e,o;const i=t.key==="Z"||t.key==="z";i&&(t.ctrlKey||t.metaKey)&&t.shiftKey?(e=this.historyActions)!=null&&e.allowRedo&&this.handleRedo():i&&(t.ctrlKey||t.metaKey)&&(o=this.historyActions)!=null&&o.allowUndo&&this.handleUndo()},document.addEventListener("vaadin-theme-updated",()=>{ye.clear(),this.refreshTheme()}),document.addEventListener("keydown",this.undoRedoListener),this.dispatchEvent(new CustomEvent("before-open"))}update(t){var e,o;super.update(t),t.has("expanded")&&(this.expanded?(this.highlightElement((e=this.context)==null?void 0:e.component.element),_e.showOverlay()):(_e.hideOverlay(),this.removeElementHighlight((o=this.context)==null?void 0:o.component.element)))}disconnectedCallback(){var t;super.disconnectedCallback(),this.removeElementHighlight((t=this.context)==null?void 0:t.component.element),_e.hideOverlay(),_e.reset(),document.removeEventListener("keydown",this.undoRedoListener),this.dispatchEvent(new CustomEvent("after-close"))}render(){var t,e,o;return this.themeEditorState===De.missing_theme?this.renderMissingThemeNotice():_`
      <div class="header">
        <div class="picker-row">
          ${this.renderPicker()}
          <div class="actions">
            ${(t=this.context)!=null&&t.metadata?_` <vaadin-dev-tools-theme-scope-selector
                  .value=${this.context.scope}
                  .metadata=${this.context.metadata}
                  @scope-change=${this.handleScopeChange}
                ></vaadin-dev-tools-theme-scope-selector>`:null}
            <button
              class="icon-button"
              data-testid="undo"
              ?disabled=${!((e=this.historyActions)!=null&&e.allowUndo)}
              @click=${this.handleUndo}
            >
              ${_t.undo}
            </button>
            <button
              class="icon-button"
              data-testid="redo"
              ?disabled=${!((o=this.historyActions)!=null&&o.allowRedo)}
              @click=${this.handleRedo}
            >
              ${_t.redo}
            </button>
          </div>
        </div>
        ${this.renderLocalClassNameEditor()}
      </div>
      ${this.renderPropertyList()}
    `}renderMissingThemeNotice(){return _`
      <div class="notice">
        It looks like you have not set up an application theme yet. Theme editor requires an existing theme to work
        with. Please check our
        <a href="https://vaadin.com/docs/latest/styling/application-theme" target="_blank">documentation</a>
        on how to set up an application theme.
      </div>
    `}renderPropertyList(){if(!this.context)return null;if(!this.context.metadata){const t=this.context.component.element.localName;return _`
        <div class="notice">Styling <code>&lt;${t}&gt;</code> components is not supported at the moment.</div>
      `}if(this.context.scope===P.local&&!this.context.accessible){const t=this.context.metadata.displayName;return _`
        ${this.context.metadata.notAccessibleDescription&&this.context.scope===P.local?_`<div class="notice hint" style="padding-bottom: 0;">
              <vaadin-icon icon="vaadin:lightbulb"></vaadin-icon>
              <div>${this.context.metadata.notAccessibleDescription}</div>
            </div>`:""}
        <div class="notice">
          The selected ${t} cannot be styled locally. Currently, Theme Editor only supports styling
          instances that are assigned to a local variable, like so:
          <pre><code>Button saveButton = new Button("Save");</code></pre>
          If you want to modify the code so that it satisfies this requirement,
          <button class="link-button" @click=${this.handleShowComponent}>click here</button>
          to open it in your IDE. Alternatively you can choose to style all ${t}s by selecting "Global" from
          the scope dropdown above.
        </div>
      `}return _` ${this.context.metadata.description&&this.context.scope===P.local?_`<div class="notice hint">
            <vaadin-icon icon="vaadin:lightbulb"></vaadin-icon>
            <div>${this.context.metadata.description}</div>
          </div>`:""}
      <vaadin-dev-tools-theme-property-list
        class="property-list"
        .metadata=${this.context.metadata}
        .theme=${this.effectiveTheme}
        @theme-property-value-change=${this.handlePropertyChange}
        @open-css=${this.handleOpenCss}
      ></vaadin-dev-tools-theme-property-list>`}handleShowComponent(){if(!this.context)return;const t=this.context.component,e={nodeId:t.nodeId,uiId:t.uiId};this.connection.sendShowComponentCreateLocation(e)}async handleOpenCss(t){if(!this.context)return;await this.ensureLocalClassName();const e={themeScope:this.context.scope,localClassName:this.context.localClassName},o=Ue(t.detail.element,e);await this.api.openCss(o)}renderPicker(){var t;let e;if((t=this.context)!=null&&t.metadata){const o=this.context.scope===P.local?this.context.metadata.displayName:`All ${this.context.metadata.displayName}s`,i=_`<span class="component-type">${o}</span>`,n=this.context.scope===P.local?xr(this.context.component):null,s=n?_` <span class="instance-name-quote">"</span><span class="instance-name">${n}</span
            ><span class="instance-name-quote">"</span>`:null;e=_`${i} ${s}`}else e=_`<span class="no-selection">Pick an element to get started</span>`;return _`
      <div class="picker">
        <button @click=${this.pickComponent}>${_t.crosshair} ${e}</button>
      </div>
    `}renderLocalClassNameEditor(){var t;const e=((t=this.context)==null?void 0:t.scope)===P.local&&this.context.accessible;if(!this.context||!e)return null;const o=this.context.localClassName||this.context.suggestedClassName;return _` <vaadin-dev-tools-theme-class-name-editor
      .className=${o}
      @class-name-change=${this.handleClassNameChange}
    >
    </vaadin-dev-tools-theme-class-name-editor>`}async handleClassNameChange(t){if(!this.context)return;const e=this.context.localClassName,o=t.detail.value;if(e){const i=this.context.component.element;this.context.localClassName=o;const n=await this.api.setLocalClassName(this.context.component,o);this.historyActions=this.history.push(n.requestId,()=>ye.previewLocalClassName(i,o),()=>ye.previewLocalClassName(i,e))}else this.context={...this.context,suggestedClassName:o}}async pickComponent(){var t;_e.hideOverlay(),this.removeElementHighlight((t=this.context)==null?void 0:t.component.element),this.pickerProvider().open({infoTemplate:_`
        <div>
          <h3>Locate the component to style</h3>
          <p>Use the mouse cursor to highlight components in the UI.</p>
          <p>Use arrow down/up to cycle through and highlight specific components under the cursor.</p>
          <p>Click the primary mouse button to select the component.</p>
        </div>
      `,pickCallback:async e=>{var o;const i=await br.getMetadata(e);if(!i){this.context={component:e,scope:((o=this.context)==null?void 0:o.scope)||P.local},this.baseTheme=null,this.editedTheme=null,this.effectiveTheme=null;return}await _e.componentPicked(e,i),this.highlightElement(e.element),this.refreshComponentAndTheme(e,i),_e.showOverlay()}})}handleScopeChange(t){this.context&&this.refreshTheme({...this.context,scope:t.detail.value})}async handlePropertyChange(t){if(!this.context||!this.baseTheme||!this.editedTheme)return;const{element:e,property:o,value:i}=t.detail;this.editedTheme.updatePropertyValue(e.selector,o.propertyName,i,!0),this.effectiveTheme=ve.combine(this.baseTheme,this.editedTheme),await this.ensureLocalClassName();const n={themeScope:this.context.scope,localClassName:this.context.localClassName},s=wr(e,n,o.propertyName,i);try{const r=await this.api.setCssRules([s]);this.historyActions=this.history.push(r.requestId);const l=Er(s);ye.add(l)}catch(r){console.error("Failed to update property value",r)}}async handleUndo(){this.historyActions=await this.history.undo(),await this.refreshComponentAndTheme()}async handleRedo(){this.historyActions=await this.history.redo(),await this.refreshComponentAndTheme()}async ensureLocalClassName(){if(!this.context||this.context.scope===P.global||this.context.localClassName)return;if(!this.context.localClassName&&!this.context.suggestedClassName)throw new Error("Cannot assign local class name for the component because it does not have a suggested class name");const t=this.context.component.element,e=this.context.suggestedClassName;this.context.localClassName=e;const o=await this.api.setLocalClassName(this.context.component,e);this.historyActions=this.history.push(o.requestId,()=>ye.previewLocalClassName(t,e),()=>ye.previewLocalClassName(t))}async refreshComponentAndTheme(t,e){var o,i,n;if(t=t||((o=this.context)==null?void 0:o.component),e=e||((i=this.context)==null?void 0:i.metadata),!t||!e)return;const s=await this.api.loadComponentMetadata(t);this.markedAsUsed||this.api.markAsUsed().then(()=>{this.markedAsUsed=!0}),ye.previewLocalClassName(t.element,s.className),await this.refreshTheme({scope:((n=this.context)==null?void 0:n.scope)||P.local,metadata:e,component:t,localClassName:s.className,suggestedClassName:s.suggestedClassName,accessible:s.accessible})}async refreshTheme(t){const e=t||this.context;if(!e||!e.metadata)return;if(e.scope===P.local&&!e.accessible){this.context=e,this.baseTheme=null,this.editedTheme=null,this.effectiveTheme=null;return}let o=new ve(e.metadata);if(!(e.scope===P.local&&!e.localClassName)){const n={themeScope:e.scope,localClassName:e.localClassName},s=e.metadata.elements.map(l=>Ue(l,n)),r=await this.api.loadRules(s);o=ve.fromServerRules(e.metadata,n,r.rules)}const i=await Sr(e.metadata);this.context=e,this.baseTheme=i,this.editedTheme=o,this.effectiveTheme=ve.combine(i,this.editedTheme)}highlightElement(t){t&&t.classList.add("vaadin-theme-editor-highlight")}removeElementHighlight(t){t&&t.classList.remove("vaadin-theme-editor-highlight")}};m([w({})],le.prototype,"expanded",void 0);m([w({})],le.prototype,"themeEditorState",void 0);m([w({})],le.prototype,"pickerProvider",void 0);m([w({})],le.prototype,"connection",void 0);m([I()],le.prototype,"historyActions",void 0);m([I()],le.prototype,"context",void 0);m([I()],le.prototype,"effectiveTheme",void 0);le=m([V("vaadin-dev-tools-theme-editor")],le);const zo=1e3,jo=(t,e)=>{const o=Array.from(t.querySelectorAll(e.join(", "))),i=Array.from(t.querySelectorAll("*")).filter(n=>n.shadowRoot).flatMap(n=>jo(n.shadowRoot,e));return[...o,...i]};let ji=!1;const tt=(t,e)=>{ji||(window.addEventListener("message",n=>{n.data==="validate-license"&&window.location.reload()},!1),ji=!0);const o=t._overlayElement;if(o){if(o.shadowRoot){const n=o.shadowRoot.querySelector("slot:not([name])");if(n&&n.assignedElements().length>0){tt(n.assignedElements()[0],e);return}}tt(o,e);return}const i=e.messageHtml?e.messageHtml:`${e.message} <p>Component: ${e.product.name} ${e.product.version}</p>`.replace(/https:([^ ]*)/g,"<a href='https:$1'>https:$1</a>");t.isConnected&&(t.outerHTML=`<no-license style="display:flex;align-items:center;text-align:center;justify-content:center;"><div>${i}</div></no-license>`)},We={},Fi={},je={},wn={},te=t=>`${t.name}_${t.version}`,Bi=t=>{const{cvdlName:e,version:o}=t.constructor,i={name:e,version:o},n=t.tagName.toLowerCase();We[e]=We[e]??[],We[e].push(n);const s=je[te(i)];s&&setTimeout(()=>tt(t,s),zo),je[te(i)]||wn[te(i)]||Fi[te(i)]||(Fi[te(i)]=!0,window.Vaadin.devTools.checkLicense(i))},_a=t=>{wn[te(t)]=!0,console.debug("License check ok for",t)},En=t=>{const e=t.product.name;je[te(t.product)]=t,console.error("License check failed for",e);const o=We[e];(o==null?void 0:o.length)>0&&jo(document,o).forEach(i=>{setTimeout(()=>tt(i,je[te(t.product)]),zo)})},ba=t=>{const e=t.message,o=t.product.name;t.messageHtml=`No license found. <a target=_blank onclick="javascript:window.open(this.href);return false;" href="${e}">Go here to start a trial or retrieve your license.</a>`,je[te(t.product)]=t,console.error("No license found when checking",o);const i=We[o];(i==null?void 0:i.length)>0&&jo(document,i).forEach(n=>{setTimeout(()=>tt(n,je[te(t.product)]),zo)})},wa=()=>{window.Vaadin.devTools.createdCvdlElements.forEach(t=>{Bi(t)}),window.Vaadin.devTools.createdCvdlElements={push:t=>{Bi(t)}}};var x;(function(t){t.ACTIVE="active",t.INACTIVE="inactive",t.UNAVAILABLE="unavailable",t.ERROR="error"})(x||(x={}));class Le extends Object{constructor(e){super(),this.status=x.UNAVAILABLE,e&&(this.webSocket=new WebSocket(e),this.webSocket.onmessage=o=>this.handleMessage(o),this.webSocket.onerror=o=>this.handleError(o),this.webSocket.onclose=o=>{this.status!==x.ERROR&&this.setStatus(x.UNAVAILABLE),this.webSocket=void 0}),setInterval(()=>{this.webSocket&&self.status!==x.ERROR&&this.status!==x.UNAVAILABLE&&this.webSocket.send("")},Le.HEARTBEAT_INTERVAL)}onHandshake(){}onReload(){}onUpdate(e,o){}onConnectionError(e){}onStatusChange(e){}onMessage(e){console.error("Unknown message received from the live reload server:",e)}handleMessage(e){let o;if(e.data!=="X"){try{o=JSON.parse(e.data)}catch(i){this.handleError(`[${i.name}: ${i.message}`);return}o.command==="hello"?(this.setStatus(x.ACTIVE),this.onHandshake()):o.command==="reload"?this.status===x.ACTIVE&&this.onReload():o.command==="update"?this.status===x.ACTIVE&&this.onUpdate(o.path,o.content):o.command==="license-check-ok"?_a(o.data):o.command==="license-check-failed"?En(o.data):o.command==="license-check-nokey"?ba(o.data):this.onMessage(o)}}handleError(e){console.error(e),this.setStatus(x.ERROR),e instanceof Event&&this.webSocket?this.onConnectionError(`Error in WebSocket connection to ${this.webSocket.url}`):this.onConnectionError(e)}setActive(e){!e&&this.status===x.ACTIVE?this.setStatus(x.INACTIVE):e&&this.status===x.INACTIVE&&this.setStatus(x.ACTIVE)}setStatus(e){this.status!==e&&(this.status=e,this.onStatusChange(e))}send(e,o){const i=JSON.stringify({command:e,data:o});this.webSocket?this.webSocket.readyState!==WebSocket.OPEN?this.webSocket.addEventListener("open",()=>this.webSocket.send(i)):this.webSocket.send(i):console.error(`Unable to send message ${e}. No websocket is available`)}setFeature(e,o){this.send("setFeature",{featureId:e,enabled:o})}sendTelemetry(e){this.send("reportTelemetry",{browserData:e})}sendLicenseCheck(e){this.send("checkLicense",e)}sendShowComponentCreateLocation(e){this.send("showComponentCreateLocation",e)}sendShowComponentAttachLocation(e){this.send("showComponentAttachLocation",e)}}Le.HEARTBEAT_INTERVAL=18e4;let Ao=class extends A{createRenderRoot(){return this}activate(){this._devTools.unreadErrors=!1,this.updateComplete.then(()=>{const t=this.renderRoot.querySelector(".message-tray .message:last-child");t&&t.scrollIntoView()})}render(){return _`<div class="message-tray">
      ${this._devTools.messages.map(t=>this._devTools.renderMessage(t))}
    </div>`}};m([w({type:Object})],Ao.prototype,"_devTools",void 0);Ao=m([V("vaadin-dev-tools-log")],Ao);var Ea=function(){var t=document.getSelection();if(!t.rangeCount)return function(){};for(var e=document.activeElement,o=[],i=0;i<t.rangeCount;i++)o.push(t.getRangeAt(i));switch(e.tagName.toUpperCase()){case"INPUT":case"TEXTAREA":e.blur();break;default:e=null;break}return t.removeAllRanges(),function(){t.type==="Caret"&&t.removeAllRanges(),t.rangeCount||o.forEach(function(n){t.addRange(n)}),e&&e.focus()}},Hi={"text/plain":"Text","text/html":"Url",default:"Text"},Sa="Copy to clipboard: #{key}, Enter";function xa(t){var e=(/mac os x/i.test(navigator.userAgent)?"⌘":"Ctrl")+"+C";return t.replace(/#{\s*key\s*}/g,e)}function Ca(t,e){var o,i,n,s,r,l,a=!1;e||(e={}),o=e.debug||!1;try{n=Ea(),s=document.createRange(),r=document.getSelection(),l=document.createElement("span"),l.textContent=t,l.style.all="unset",l.style.position="fixed",l.style.top=0,l.style.clip="rect(0, 0, 0, 0)",l.style.whiteSpace="pre",l.style.webkitUserSelect="text",l.style.MozUserSelect="text",l.style.msUserSelect="text",l.style.userSelect="text",l.addEventListener("copy",function(h){if(h.stopPropagation(),e.format)if(h.preventDefault(),typeof h.clipboardData>"u"){o&&console.warn("unable to use e.clipboardData"),o&&console.warn("trying IE specific stuff"),window.clipboardData.clearData();var v=Hi[e.format]||Hi.default;window.clipboardData.setData(v,t)}else h.clipboardData.clearData(),h.clipboardData.setData(e.format,t);e.onCopy&&(h.preventDefault(),e.onCopy(h.clipboardData))}),document.body.appendChild(l),s.selectNodeContents(l),r.addRange(s);var d=document.execCommand("copy");if(!d)throw new Error("copy command was unsuccessful");a=!0}catch(h){o&&console.error("unable to copy using execCommand: ",h),o&&console.warn("trying IE specific stuff");try{window.clipboardData.setData(e.format||"text",t),e.onCopy&&e.onCopy(window.clipboardData),a=!0}catch(v){o&&console.error("unable to copy using clipboardData: ",v),o&&console.error("falling back to prompt"),i=xa("message"in e?e.message:Sa),window.prompt(i,t)}}finally{r&&(typeof r.removeRange=="function"?r.removeRange(s):r.removeAllRanges()),l&&document.body.removeChild(l),n()}return a}let Lt=class extends A{constructor(){super(...arguments),this.serverInfo={versions:[]}}createRenderRoot(){return this}render(){return _` <div class="info-tray">
      <button class="button copy" @click=${this.copyInfoToClipboard}>Copy</button>
      <dl>
        ${this.serverInfo.versions.map(t=>_`
            <dt>${t.name}</dt>
            <dd>${t.version}</dd>
          `)}
        <dt>Browser</dt>
        <dd>${navigator.userAgent}</dd>
        <dt>
          Live reload
          <label class="switch">
            <input
              id="toggle"
              type="checkbox"
              ?disabled=${this._devTools.liveReloadDisabled||(this._devTools.frontendStatus===x.UNAVAILABLE||this._devTools.frontendStatus===x.ERROR)&&(this._devTools.javaStatus===x.UNAVAILABLE||this._devTools.javaStatus===x.ERROR)}
              ?checked="${this._devTools.frontendStatus===x.ACTIVE||this._devTools.javaStatus===x.ACTIVE}"
              @change=${t=>this._devTools.setActive(t.target.checked)}
            />
            <span class="slider"></span>
          </label>
        </dt>
        <dd
          class="live-reload-status"
          style="--status-color: ${this._devTools.getStatusColor(this._devTools.javaStatus)}"
        >
          Java ${this._devTools.javaStatus}
          ${this._devTools.backend?`(${S.BACKEND_DISPLAY_NAME[this._devTools.backend]})`:""}
        </dd>
        <dd
          class="live-reload-status"
          style="--status-color: ${this._devTools.getStatusColor(this._devTools.frontendStatus)}"
        >
          Front end ${this._devTools.frontendStatus}
        </dd>
      </dl>
    </div>`}handleMessage(t){return(t==null?void 0:t.command)==="serverInfo"?(this.serverInfo=t.data,!0):!1}copyInfoToClipboard(){const t=this.renderRoot.querySelectorAll(".info-tray dt, .info-tray dd"),e=Array.from(t).map(o=>(o.localName==="dd"?": ":`
`)+o.textContent.trim()).join("").replace(/^\n/,"");Ca(e),this._devTools.showNotification(j.INFORMATION,"Environment information copied to clipboard",void 0,void 0,"versionInfoCopied")}};m([w({type:Object})],Lt.prototype,"_devTools",void 0);m([I()],Lt.prototype,"serverInfo",void 0);Lt=m([V("vaadin-dev-tools-info")],Lt);var k,j;(function(t){t.LOG="log",t.INFORMATION="information",t.WARNING="warning",t.ERROR="error"})(j||(j={}));let S=k=class extends A{static get styles(){return[$`
        :host {
          --dev-tools-font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell,
            'Helvetica Neue', sans-serif;
          --dev-tools-font-family-monospace: SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New',
            monospace;

          --dev-tools-font-size: 0.8125rem;
          --dev-tools-font-size-small: 0.75rem;

          --dev-tools-text-color: rgba(255, 255, 255, 0.8);
          --dev-tools-text-color-secondary: rgba(255, 255, 255, 0.65);
          --dev-tools-text-color-emphasis: rgba(255, 255, 255, 0.95);
          --dev-tools-text-color-active: rgba(255, 255, 255, 1);

          --dev-tools-background-color-inactive: rgba(45, 45, 45, 0.25);
          --dev-tools-background-color-active: rgba(45, 45, 45, 0.98);
          --dev-tools-background-color-active-blurred: rgba(45, 45, 45, 0.85);

          --dev-tools-border-radius: 0.5rem;
          --dev-tools-box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.05), 0 4px 12px -2px rgba(0, 0, 0, 0.4);

          --dev-tools-blue-hsl: 206, 100%, 70%;
          --dev-tools-blue-color: hsl(var(--dev-tools-blue-hsl));
          --dev-tools-green-hsl: 145, 80%, 42%;
          --dev-tools-green-color: hsl(var(--dev-tools-green-hsl));
          --dev-tools-grey-hsl: 0, 0%, 50%;
          --dev-tools-grey-color: hsl(var(--dev-tools-grey-hsl));
          --dev-tools-yellow-hsl: 38, 98%, 64%;
          --dev-tools-yellow-color: hsl(var(--dev-tools-yellow-hsl));
          --dev-tools-red-hsl: 355, 100%, 68%;
          --dev-tools-red-color: hsl(var(--dev-tools-red-hsl));

          /* Needs to be in ms, used in JavaScript as well */
          --dev-tools-transition-duration: 180ms;

          all: initial;

          direction: ltr;
          cursor: default;
          font: normal 400 var(--dev-tools-font-size) / 1.125rem var(--dev-tools-font-family);
          color: var(--dev-tools-text-color);
          -webkit-user-select: none;
          -moz-user-select: none;
          user-select: none;
          color-scheme: dark;

          position: fixed;
          z-index: 20000;
          pointer-events: none;
          bottom: 0;
          right: 0;
          width: 100%;
          height: 100%;
          display: flex;
          flex-direction: column-reverse;
          align-items: flex-end;
        }

        .dev-tools {
          pointer-events: auto;
          display: flex;
          align-items: center;
          position: fixed;
          z-index: inherit;
          right: 0.5rem;
          bottom: 0.5rem;
          min-width: 1.75rem;
          height: 1.75rem;
          max-width: 1.75rem;
          border-radius: 0.5rem;
          padding: 0.375rem;
          box-sizing: border-box;
          background-color: var(--dev-tools-background-color-inactive);
          box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.05);
          color: var(--dev-tools-text-color);
          transition: var(--dev-tools-transition-duration);
          white-space: nowrap;
          line-height: 1rem;
        }

        .dev-tools:hover,
        .dev-tools.active {
          background-color: var(--dev-tools-background-color-active);
          box-shadow: var(--dev-tools-box-shadow);
        }

        .dev-tools.active {
          max-width: calc(100% - 1rem);
        }

        .dev-tools .dev-tools-icon {
          flex: none;
          pointer-events: none;
          display: inline-block;
          width: 1rem;
          height: 1rem;
          fill: #fff;
          transition: var(--dev-tools-transition-duration);
          margin: 0;
        }

        .dev-tools.active .dev-tools-icon {
          opacity: 0;
          position: absolute;
          transform: scale(0.5);
        }

        .dev-tools .status-blip {
          flex: none;
          display: block;
          width: 6px;
          height: 6px;
          border-radius: 50%;
          z-index: 20001;
          background: var(--dev-tools-grey-color);
          position: absolute;
          top: -1px;
          right: -1px;
        }

        .dev-tools .status-description {
          overflow: hidden;
          text-overflow: ellipsis;
          padding: 0 0.25rem;
        }

        .dev-tools.error {
          background-color: hsla(var(--dev-tools-red-hsl), 0.15);
          animation: bounce 0.5s;
          animation-iteration-count: 2;
        }

        .switch {
          display: inline-flex;
          align-items: center;
        }

        .switch input {
          opacity: 0;
          width: 0;
          height: 0;
          position: absolute;
        }

        .switch .slider {
          display: block;
          flex: none;
          width: 28px;
          height: 18px;
          border-radius: 9px;
          background-color: rgba(255, 255, 255, 0.3);
          transition: var(--dev-tools-transition-duration);
          margin-right: 0.5rem;
        }

        .switch:focus-within .slider,
        .switch .slider:hover {
          background-color: rgba(255, 255, 255, 0.35);
          transition: none;
        }

        .switch input:focus-visible ~ .slider {
          box-shadow: 0 0 0 2px var(--dev-tools-background-color-active), 0 0 0 4px var(--dev-tools-blue-color);
        }

        .switch .slider::before {
          content: '';
          display: block;
          margin: 2px;
          width: 14px;
          height: 14px;
          background-color: #fff;
          transition: var(--dev-tools-transition-duration);
          border-radius: 50%;
        }

        .switch input:checked + .slider {
          background-color: var(--dev-tools-green-color);
        }

        .switch input:checked + .slider::before {
          transform: translateX(10px);
        }

        .switch input:disabled + .slider::before {
          background-color: var(--dev-tools-grey-color);
        }

        .window.hidden {
          opacity: 0;
          transform: scale(0);
          position: absolute;
        }

        .window.visible {
          transform: none;
          opacity: 1;
          pointer-events: auto;
        }

        .window.visible ~ .dev-tools {
          opacity: 0;
          pointer-events: none;
        }

        .window.visible ~ .dev-tools .dev-tools-icon,
        .window.visible ~ .dev-tools .status-blip {
          transition: none;
          opacity: 0;
        }

        .window {
          border-radius: var(--dev-tools-border-radius);
          overflow: auto;
          margin: 0.5rem;
          min-width: 30rem;
          max-width: calc(100% - 1rem);
          max-height: calc(100vh - 1rem);
          flex-shrink: 1;
          background-color: var(--dev-tools-background-color-active);
          color: var(--dev-tools-text-color);
          transition: var(--dev-tools-transition-duration);
          transform-origin: bottom right;
          display: flex;
          flex-direction: column;
          box-shadow: var(--dev-tools-box-shadow);
          outline: none;
        }

        .window-toolbar {
          display: flex;
          flex: none;
          align-items: center;
          padding: 0.375rem;
          white-space: nowrap;
          order: 1;
          background-color: rgba(0, 0, 0, 0.2);
          gap: 0.5rem;
        }

        .tab {
          color: var(--dev-tools-text-color-secondary);
          font: inherit;
          font-size: var(--dev-tools-font-size-small);
          font-weight: 500;
          line-height: 1;
          padding: 0.25rem 0.375rem;
          background: none;
          border: none;
          margin: 0;
          border-radius: 0.25rem;
          transition: var(--dev-tools-transition-duration);
        }

        .tab:hover,
        .tab.active {
          color: var(--dev-tools-text-color-active);
        }

        .tab.active {
          background-color: rgba(255, 255, 255, 0.12);
        }

        .tab.unreadErrors::after {
          content: '•';
          color: hsl(var(--dev-tools-red-hsl));
          font-size: 1.5rem;
          position: absolute;
          transform: translate(0, -50%);
        }

        .ahreflike {
          font-weight: 500;
          color: var(--dev-tools-text-color-secondary);
          text-decoration: underline;
          cursor: pointer;
        }

        .ahreflike:hover {
          color: var(--dev-tools-text-color-emphasis);
        }

        .button {
          all: initial;
          font-family: inherit;
          font-size: var(--dev-tools-font-size-small);
          line-height: 1;
          white-space: nowrap;
          background-color: rgba(0, 0, 0, 0.2);
          color: inherit;
          font-weight: 600;
          padding: 0.25rem 0.375rem;
          border-radius: 0.25rem;
        }

        .button:focus,
        .button:hover {
          color: var(--dev-tools-text-color-emphasis);
        }

        .minimize-button {
          flex: none;
          width: 1rem;
          height: 1rem;
          color: inherit;
          background-color: transparent;
          border: 0;
          padding: 0;
          margin: 0 0 0 auto;
          opacity: 0.8;
        }

        .minimize-button:hover {
          opacity: 1;
        }

        .minimize-button svg {
          max-width: 100%;
        }

        .message.information {
          --dev-tools-notification-color: var(--dev-tools-blue-color);
        }

        .message.warning {
          --dev-tools-notification-color: var(--dev-tools-yellow-color);
        }

        .message.error {
          --dev-tools-notification-color: var(--dev-tools-red-color);
        }

        .message {
          display: flex;
          padding: 0.1875rem 0.75rem 0.1875rem 2rem;
          background-clip: padding-box;
        }

        .message.log {
          padding-left: 0.75rem;
        }

        .message-content {
          margin-right: 0.5rem;
          -webkit-user-select: text;
          -moz-user-select: text;
          user-select: text;
        }

        .message-heading {
          position: relative;
          display: flex;
          align-items: center;
          margin: 0.125rem 0;
        }

        .message.log {
          color: var(--dev-tools-text-color-secondary);
        }

        .message:not(.log) .message-heading {
          font-weight: 500;
        }

        .message.has-details .message-heading {
          color: var(--dev-tools-text-color-emphasis);
          font-weight: 600;
        }

        .message-heading::before {
          position: absolute;
          margin-left: -1.5rem;
          display: inline-block;
          text-align: center;
          font-size: 0.875em;
          font-weight: 600;
          line-height: calc(1.25em - 2px);
          width: 14px;
          height: 14px;
          box-sizing: border-box;
          border: 1px solid transparent;
          border-radius: 50%;
        }

        .message.information .message-heading::before {
          content: 'i';
          border-color: currentColor;
          color: var(--dev-tools-notification-color);
        }

        .message.warning .message-heading::before,
        .message.error .message-heading::before {
          content: '!';
          color: var(--dev-tools-background-color-active);
          background-color: var(--dev-tools-notification-color);
        }

        .features-tray {
          padding: 0.75rem;
          flex: auto;
          overflow: auto;
          animation: fade-in var(--dev-tools-transition-duration) ease-in;
          user-select: text;
        }

        .features-tray p {
          margin-top: 0;
          color: var(--dev-tools-text-color-secondary);
        }

        .features-tray .feature {
          display: flex;
          align-items: center;
          gap: 1rem;
          padding-bottom: 0.5em;
        }

        .message .message-details {
          font-weight: 400;
          color: var(--dev-tools-text-color-secondary);
          margin: 0.25rem 0;
        }

        .message .message-details[hidden] {
          display: none;
        }

        .message .message-details p {
          display: inline;
          margin: 0;
          margin-right: 0.375em;
          word-break: break-word;
        }

        .message .persist {
          color: var(--dev-tools-text-color-secondary);
          white-space: nowrap;
          margin: 0.375rem 0;
          display: flex;
          align-items: center;
          position: relative;
          -webkit-user-select: none;
          -moz-user-select: none;
          user-select: none;
        }

        .message .persist::before {
          content: '';
          width: 1em;
          height: 1em;
          border-radius: 0.2em;
          margin-right: 0.375em;
          background-color: rgba(255, 255, 255, 0.3);
        }

        .message .persist:hover::before {
          background-color: rgba(255, 255, 255, 0.4);
        }

        .message .persist.on::before {
          background-color: rgba(255, 255, 255, 0.9);
        }

        .message .persist.on::after {
          content: '';
          order: -1;
          position: absolute;
          width: 0.75em;
          height: 0.25em;
          border: 2px solid var(--dev-tools-background-color-active);
          border-width: 0 0 2px 2px;
          transform: translate(0.05em, -0.05em) rotate(-45deg) scale(0.8, 0.9);
        }

        .message .dismiss-message {
          font-weight: 600;
          align-self: stretch;
          display: flex;
          align-items: center;
          padding: 0 0.25rem;
          margin-left: 0.5rem;
          color: var(--dev-tools-text-color-secondary);
        }

        .message .dismiss-message:hover {
          color: var(--dev-tools-text-color);
        }

        .notification-tray {
          display: flex;
          flex-direction: column-reverse;
          align-items: flex-end;
          margin: 0.5rem;
          flex: none;
        }

        .window.hidden + .notification-tray {
          margin-bottom: 3rem;
        }

        .notification-tray .message {
          pointer-events: auto;
          background-color: var(--dev-tools-background-color-active);
          color: var(--dev-tools-text-color);
          max-width: 30rem;
          box-sizing: border-box;
          border-radius: var(--dev-tools-border-radius);
          margin-top: 0.5rem;
          transition: var(--dev-tools-transition-duration);
          transform-origin: bottom right;
          animation: slideIn var(--dev-tools-transition-duration);
          box-shadow: var(--dev-tools-box-shadow);
          padding-top: 0.25rem;
          padding-bottom: 0.25rem;
        }

        .notification-tray .message.animate-out {
          animation: slideOut forwards var(--dev-tools-transition-duration);
        }

        .notification-tray .message .message-details {
          max-height: 10em;
          overflow: hidden;
        }

        .message-tray {
          flex: auto;
          overflow: auto;
          max-height: 20rem;
          user-select: text;
        }

        .message-tray .message {
          animation: fade-in var(--dev-tools-transition-duration) ease-in;
          padding-left: 2.25rem;
        }

        .message-tray .message.warning {
          background-color: hsla(var(--dev-tools-yellow-hsl), 0.09);
        }

        .message-tray .message.error {
          background-color: hsla(var(--dev-tools-red-hsl), 0.09);
        }

        .message-tray .message.error .message-heading {
          color: hsl(var(--dev-tools-red-hsl));
        }

        .message-tray .message.warning .message-heading {
          color: hsl(var(--dev-tools-yellow-hsl));
        }

        .message-tray .message + .message {
          border-top: 1px solid rgba(255, 255, 255, 0.07);
        }

        .message-tray .dismiss-message,
        .message-tray .persist {
          display: none;
        }

        .info-tray {
          padding: 0.75rem;
          position: relative;
          flex: auto;
          overflow: auto;
          animation: fade-in var(--dev-tools-transition-duration) ease-in;
          user-select: text;
        }

        .info-tray dl {
          margin: 0;
          display: grid;
          grid-template-columns: max-content 1fr;
          column-gap: 0.75rem;
          position: relative;
        }

        .info-tray dt {
          grid-column: 1;
          color: var(--dev-tools-text-color-emphasis);
        }

        .info-tray dt:not(:first-child)::before {
          content: '';
          width: 100%;
          position: absolute;
          height: 1px;
          background-color: rgba(255, 255, 255, 0.1);
          margin-top: -0.375rem;
        }

        .info-tray dd {
          grid-column: 2;
          margin: 0;
        }

        .info-tray :is(dt, dd):not(:last-child) {
          margin-bottom: 0.75rem;
        }

        .info-tray dd + dd {
          margin-top: -0.5rem;
        }

        .info-tray .live-reload-status::before {
          content: '•';
          color: var(--status-color);
          width: 0.75rem;
          display: inline-block;
          font-size: 1rem;
          line-height: 0.5rem;
        }

        .info-tray .copy {
          position: fixed;
          z-index: 1;
          top: 0.5rem;
          right: 0.5rem;
        }

        .info-tray .switch {
          vertical-align: -4px;
        }

        @keyframes slideIn {
          from {
            transform: translateX(100%);
            opacity: 0;
          }
          to {
            transform: translateX(0%);
            opacity: 1;
          }
        }

        @keyframes slideOut {
          from {
            transform: translateX(0%);
            opacity: 1;
          }
          to {
            transform: translateX(100%);
            opacity: 0;
          }
        }

        @keyframes fade-in {
          0% {
            opacity: 0;
          }
        }

        @keyframes bounce {
          0% {
            transform: scale(0.8);
          }
          50% {
            transform: scale(1.5);
            background-color: hsla(var(--dev-tools-red-hsl), 1);
          }
          100% {
            transform: scale(1);
          }
        }

        @supports (backdrop-filter: blur(1px)) {
          .dev-tools,
          .window,
          .notification-tray .message {
            backdrop-filter: blur(8px);
          }
          .dev-tools:hover,
          .dev-tools.active,
          .window,
          .notification-tray .message {
            background-color: var(--dev-tools-background-color-active-blurred);
          }
        }
      `,bn]}static get isActive(){const t=window.sessionStorage.getItem(k.ACTIVE_KEY_IN_SESSION_STORAGE);return t===null||t!=="false"}static notificationDismissed(t){const e=window.localStorage.getItem(k.DISMISSED_NOTIFICATIONS_IN_LOCAL_STORAGE);return e!==null&&e.includes(t)}elementTelemetry(){let t={};try{const e=localStorage.getItem("vaadin.statistics.basket");if(!e)return;t=JSON.parse(e)}catch{return}this.frontendConnection&&this.frontendConnection.sendTelemetry(t)}openWebSocketConnection(){this.frontendStatus=x.UNAVAILABLE,this.javaStatus=x.UNAVAILABLE;const t=l=>this.log(j.ERROR,l),e=()=>{this.showSplashMessage("Reloading…");const l=window.sessionStorage.getItem(k.TRIGGERED_COUNT_KEY_IN_SESSION_STORAGE),a=l?parseInt(l,10)+1:1;window.sessionStorage.setItem(k.TRIGGERED_COUNT_KEY_IN_SESSION_STORAGE,a.toString()),window.sessionStorage.setItem(k.TRIGGERED_KEY_IN_SESSION_STORAGE,"true"),window.location.reload()},o=(l,a)=>{let d=document.head.querySelector(`style[data-file-path='${l}']`);d?(this.log(j.INFORMATION,"Hot update of "+l),d.textContent=a,document.dispatchEvent(new CustomEvent("vaadin-theme-updated"))):e()},i=new Le(this.getDedicatedWebSocketUrl());i.onHandshake=()=>{this.log(j.LOG,"Vaadin development mode initialized"),k.isActive||i.setActive(!1),this.elementTelemetry()},i.onConnectionError=t,i.onReload=e,i.onUpdate=o,i.onStatusChange=l=>{this.frontendStatus=l},i.onMessage=l=>this.handleFrontendMessage(l),this.frontendConnection=i;let n;this.backend===k.SPRING_BOOT_DEVTOOLS&&this.springBootLiveReloadPort?(n=new Le(this.getSpringBootWebSocketUrl(window.location)),n.onHandshake=()=>{k.isActive||n.setActive(!1)},n.onReload=e,n.onConnectionError=t):this.backend===k.JREBEL||this.backend===k.HOTSWAP_AGENT?n=i:n=new Le(void 0);const s=n.onStatusChange;n.onStatusChange=l=>{s(l),this.javaStatus=l};const r=n.onHandshake;n.onHandshake=()=>{r(),this.backend&&this.log(j.INFORMATION,`Java live reload available: ${k.BACKEND_DISPLAY_NAME[this.backend]}`)},this.javaConnection=n,this.backend||this.showNotification(j.WARNING,"Java live reload unavailable","Live reload for Java changes is currently not set up. Find out how to make use of this functionality to boost your workflow.","https://vaadin.com/docs/latest/flow/configuration/live-reload","liveReloadUnavailable")}tabHandleMessage(t,e){const o=t;return o.handleMessage&&o.handleMessage.call(t,e)}handleFrontendMessage(t){for(const e of this.tabs)if(e.element&&this.tabHandleMessage(e.element,t))return;if((t==null?void 0:t.command)==="featureFlags")this.features=t.data.features;else if((t==null?void 0:t.command)==="themeEditorState"){const e=!!window.Vaadin.Flow;this.themeEditorState=t.data,e&&this.themeEditorState!==De.disabled&&(this.tabs.push({id:"theme-editor",title:"Theme Editor (Preview)",render:()=>this.renderThemeEditor()}),this.requestUpdate())}else this.unhandledMessages.push(t)}getDedicatedWebSocketUrl(){function t(o){const i=document.createElement("div");return i.innerHTML=`<a href="${o}"/>`,i.firstChild.href}if(this.url===void 0)return;const e=t(this.url);if(!e.startsWith("http://")&&!e.startsWith("https://")){console.error("The protocol of the url should be http or https for live reload to work.");return}return`${e.replace(/^http/,"ws")}?v-r=push&debug_window`}getSpringBootWebSocketUrl(t){const{hostname:e}=t,o=t.protocol==="https:"?"wss":"ws";if(e.endsWith("gitpod.io")){const i=e.replace(/.*?-/,"");return`${o}://${this.springBootLiveReloadPort}-${i}`}else return`${o}://${e}:${this.springBootLiveReloadPort}`}constructor(){super(),this.unhandledMessages=[],this.expanded=!1,this.messages=[],this.notifications=[],this.frontendStatus=x.UNAVAILABLE,this.javaStatus=x.UNAVAILABLE,this.tabs=[{id:"log",title:"Log",render:"vaadin-dev-tools-log"},{id:"info",title:"Info",render:"vaadin-dev-tools-info"},{id:"features",title:"Feature Flags",render:()=>this.renderFeatures()}],this.activeTab="log",this.features=[],this.unreadErrors=!1,this.componentPickActive=!1,this.themeEditorState=De.disabled,this.nextMessageId=1,this.transitionDuration=0,window.Vaadin.Flow&&this.tabs.push({id:"code",title:"Code",render:()=>this.renderCode()})}connectedCallback(){if(super.connectedCallback(),this.catchErrors(),this.disableEventListener=o=>this.demoteSplashMessage(),document.body.addEventListener("focus",this.disableEventListener),document.body.addEventListener("click",this.disableEventListener),window.sessionStorage.getItem(k.TRIGGERED_KEY_IN_SESSION_STORAGE)){const o=new Date,i=`${`0${o.getHours()}`.slice(-2)}:${`0${o.getMinutes()}`.slice(-2)}:${`0${o.getSeconds()}`.slice(-2)}`;this.showSplashMessage(`Page reloaded at ${i}`),window.sessionStorage.removeItem(k.TRIGGERED_KEY_IN_SESSION_STORAGE)}this.transitionDuration=parseInt(window.getComputedStyle(this).getPropertyValue("--dev-tools-transition-duration"),10);const t=window;t.Vaadin=t.Vaadin||{},t.Vaadin.devTools=Object.assign(this,t.Vaadin.devTools),document.documentElement.addEventListener("vaadin-overlay-outside-click",o=>{const i=o,n=i.target.owner;n&&dr(this,n)||i.detail.sourceEvent.composedPath().includes(this)&&o.preventDefault()});const e=window.Vaadin;e.devToolsPlugins&&(Array.from(e.devToolsPlugins).forEach(o=>this.initPlugin(o)),e.devToolsPlugins={push:o=>this.initPlugin(o)}),this.openWebSocketConnection(),wa()}async initPlugin(t){const e=this;t.init({addTab:(o,i)=>{e.tabs.push({id:o,title:o,render:i})},send:function(o,i){e.frontendConnection.send(o,i)}})}format(t){return t.toString()}catchErrors(){const t=window.Vaadin.ConsoleErrors;t&&t.forEach(e=>{this.log(j.ERROR,e.map(o=>this.format(o)).join(" "))}),window.Vaadin.ConsoleErrors={push:e=>{this.log(j.ERROR,e.map(o=>this.format(o)).join(" "))}}}disconnectedCallback(){this.disableEventListener&&(document.body.removeEventListener("focus",this.disableEventListener),document.body.removeEventListener("click",this.disableEventListener)),super.disconnectedCallback()}toggleExpanded(){this.notifications.slice().forEach(t=>this.dismissNotification(t.id)),this.expanded=!this.expanded,this.expanded&&this.root.focus()}showSplashMessage(t){this.splashMessage=t,this.splashMessage&&(this.expanded?this.demoteSplashMessage():setTimeout(()=>{this.demoteSplashMessage()},k.AUTO_DEMOTE_NOTIFICATION_DELAY))}demoteSplashMessage(){this.splashMessage&&this.log(j.LOG,this.splashMessage),this.showSplashMessage(void 0)}checkLicense(t){this.frontendConnection?this.frontendConnection.sendLicenseCheck(t):En({message:"Internal error: no connection",product:t})}log(t,e,o,i){const n=this.nextMessageId;for(this.nextMessageId+=1,this.messages.push({id:n,type:t,message:e,details:o,link:i,dontShowAgain:!1,deleted:!1});this.messages.length>k.MAX_LOG_ROWS;)this.messages.shift();this.requestUpdate(),this.updateComplete.then(()=>{const s=this.renderRoot.querySelector(".message-tray .message:last-child");this.expanded&&s?(setTimeout(()=>s.scrollIntoView({behavior:"smooth"}),this.transitionDuration),this.unreadErrors=!1):t===j.ERROR&&(this.unreadErrors=!0)})}showNotification(t,e,o,i,n){if(n===void 0||!k.notificationDismissed(n)){if(this.notifications.filter(r=>r.persistentId===n).filter(r=>!r.deleted).length>0)return;const s=this.nextMessageId;this.nextMessageId+=1,this.notifications.push({id:s,type:t,message:e,details:o,link:i,persistentId:n,dontShowAgain:!1,deleted:!1}),i===void 0&&setTimeout(()=>{this.dismissNotification(s)},k.AUTO_DEMOTE_NOTIFICATION_DELAY),this.requestUpdate()}else this.log(t,e,o,i)}dismissNotification(t){const e=this.findNotificationIndex(t);if(e!==-1&&!this.notifications[e].deleted){const o=this.notifications[e];if(o.dontShowAgain&&o.persistentId&&!k.notificationDismissed(o.persistentId)){let i=window.localStorage.getItem(k.DISMISSED_NOTIFICATIONS_IN_LOCAL_STORAGE);i=i===null?o.persistentId:`${i},${o.persistentId}`,window.localStorage.setItem(k.DISMISSED_NOTIFICATIONS_IN_LOCAL_STORAGE,i)}o.deleted=!0,this.log(o.type,o.message,o.details,o.link),setTimeout(()=>{const i=this.findNotificationIndex(t);i!==-1&&(this.notifications.splice(i,1),this.requestUpdate())},this.transitionDuration)}}findNotificationIndex(t){let e=-1;return this.notifications.some((o,i)=>o.id===t?(e=i,!0):!1),e}toggleDontShowAgain(t){const e=this.findNotificationIndex(t);if(e!==-1&&!this.notifications[e].deleted){const o=this.notifications[e];o.dontShowAgain=!o.dontShowAgain,this.requestUpdate()}}setActive(t){var e,o;(e=this.frontendConnection)==null||e.setActive(t),(o=this.javaConnection)==null||o.setActive(t),window.sessionStorage.setItem(k.ACTIVE_KEY_IN_SESSION_STORAGE,t?"true":"false")}getStatusColor(t){return t===x.ACTIVE?"var(--dev-tools-green-color)":t===x.INACTIVE?"var(--dev-tools-grey-color)":t===x.UNAVAILABLE?"var(--dev-tools-yellow-hsl)":t===x.ERROR?"var(--dev-tools-red-color)":"none"}renderMessage(t){return _`
      <div
        class="message ${t.type} ${t.deleted?"animate-out":""} ${t.details||t.link?"has-details":""}"
      >
        <div class="message-content">
          <div class="message-heading">${t.message}</div>
          <div class="message-details" ?hidden="${!t.details&&!t.link}">
            ${t.details?_`<p>${t.details}</p>`:""}
            ${t.link?_`<a class="ahreflike" href="${t.link}" target="_blank">Learn more</a>`:""}
          </div>
          ${t.persistentId?_`<div
                class="persist ${t.dontShowAgain?"on":"off"}"
                @click=${()=>this.toggleDontShowAgain(t.id)}
              >
                Don’t show again
              </div>`:""}
        </div>
        <div class="dismiss-message" @click=${()=>this.dismissNotification(t.id)}>Dismiss</div>
      </div>
    `}render(){return _` <div
        class="window ${this.expanded&&!this.componentPickActive?"visible":"hidden"}"
        tabindex="0"
        @keydown=${t=>t.key==="Escape"&&this.expanded&&this.toggleExpanded()}
      >
        <div class="window-toolbar">
          ${this.tabs.map(t=>_`<button
                class=${Mo({tab:!0,active:this.activeTab===t.id,unreadErrors:t.id==="log"&&this.unreadErrors})}
                id="${t.id}"
                @click=${()=>{const e=this.tabs.find(n=>n.id===this.activeTab);if(e&&e.element){const n=typeof e.render=="function"?e.element.firstElementChild:e.element,s=n==null?void 0:n.deactivate;s&&s.call(n)}this.activeTab=t.id;const o=typeof t.render=="function"?t.element.firstElementChild:t.element,i=o.activate;i&&i.call(o)}}
              >
                ${t.title}
              </button> `)}
          <button class="minimize-button" title="Minimize" @click=${()=>this.toggleExpanded()}>
            <svg fill="none" height="16" viewBox="0 0 16 16" width="16" xmlns="http://www.w3.org/2000/svg">
              <g fill="#fff" opacity=".8">
                <path
                  d="m7.25 1.75c0-.41421.33579-.75.75-.75h3.25c2.0711 0 3.75 1.67893 3.75 3.75v6.5c0 2.0711-1.6789 3.75-3.75 3.75h-6.5c-2.07107 0-3.75-1.6789-3.75-3.75v-3.25c0-.41421.33579-.75.75-.75s.75.33579.75.75v3.25c0 1.2426 1.00736 2.25 2.25 2.25h6.5c1.2426 0 2.25-1.0074 2.25-2.25v-6.5c0-1.24264-1.0074-2.25-2.25-2.25h-3.25c-.41421 0-.75-.33579-.75-.75z"
                />
                <path
                  d="m2.96967 2.96967c.29289-.29289.76777-.29289 1.06066 0l5.46967 5.46967v-2.68934c0-.41421.33579-.75.75-.75.4142 0 .75.33579.75.75v4.5c0 .4142-.3358.75-.75.75h-4.5c-.41421 0-.75-.3358-.75-.75 0-.41421.33579-.75.75-.75h2.68934l-5.46967-5.46967c-.29289-.29289-.29289-.76777 0-1.06066z"
                />
              </g>
            </svg>
          </button>
        </div>
        <div id="tabContainer"></div>
      </div>

      <div class="notification-tray">${this.notifications.map(t=>this.renderMessage(t))}</div>
      <vaadin-dev-tools-component-picker
        .active=${this.componentPickActive}
        @component-picker-opened=${()=>{this.componentPickActive=!0}}
        @component-picker-closed=${()=>{this.componentPickActive=!1}}
      ></vaadin-dev-tools-component-picker>
      <div
        class="dev-tools ${this.splashMessage?"active":""}${this.unreadErrors?" error":""}"
        @click=${()=>this.toggleExpanded()}
      >
        ${this.unreadErrors?_`<svg
              fill="none"
              height="16"
              viewBox="0 0 16 16"
              width="16"
              xmlns="http://www.w3.org/2000/svg"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              class="dev-tools-icon error"
            >
              <clipPath id="a"><path d="m0 0h16v16h-16z" /></clipPath>
              <g clip-path="url(#a)">
                <path
                  d="m6.25685 2.09894c.76461-1.359306 2.72169-1.359308 3.4863 0l5.58035 9.92056c.7499 1.3332-.2135 2.9805-1.7432 2.9805h-11.1606c-1.529658 0-2.4930857-1.6473-1.743156-2.9805z"
                  fill="#ff5c69"
                />
                <path
                  d="m7.99699 4c-.45693 0-.82368.37726-.81077.834l.09533 3.37352c.01094.38726.32803.69551.71544.69551.38741 0 .70449-.30825.71544-.69551l.09533-3.37352c.0129-.45674-.35384-.834-.81077-.834zm.00301 8c.60843 0 1-.3879 1-.979 0-.5972-.39157-.9851-1-.9851s-1 .3879-1 .9851c0 .5911.39157.979 1 .979z"
                  fill="#fff"
                />
              </g>
            </svg>`:_`<svg
              fill="none"
              height="17"
              viewBox="0 0 16 17"
              width="16"
              xmlns="http://www.w3.org/2000/svg"
              class="dev-tools-icon logo"
            >
              <g fill="#fff">
                <path
                  d="m8.88273 5.97926c0 .04401-.0032.08898-.00801.12913-.02467.42848-.37813.76767-.8117.76767-.43358 0-.78704-.34112-.81171-.76928-.00481-.04015-.00801-.08351-.00801-.12752 0-.42784-.10255-.87656-1.14434-.87656h-3.48364c-1.57118 0-2.315271-.72849-2.315271-2.21758v-1.26683c0-.42431.324618-.768314.748261-.768314.42331 0 .74441.344004.74441.768314v.42784c0 .47924.39576.81265 1.11293.81265h3.41538c1.5542 0 1.67373 1.156 1.725 1.7679h.03429c.05095-.6119.17048-1.7679 1.72468-1.7679h3.4154c.7172 0 1.0145-.32924 1.0145-.80847l-.0067-.43202c0-.42431.3227-.768314.7463-.768314.4234 0 .7255.344004.7255.768314v1.26683c0 1.48909-.6181 2.21758-2.1893 2.21758h-3.4836c-1.04182 0-1.14437.44872-1.14437.87656z"
                />
                <path
                  d="m8.82577 15.1648c-.14311.3144-.4588.5335-.82635.5335-.37268 0-.69252-.2249-.83244-.5466-.00206-.0037-.00412-.0073-.00617-.0108-.00275-.0047-.00549-.0094-.00824-.0145l-3.16998-5.87318c-.08773-.15366-.13383-.32816-.13383-.50395 0-.56168.45592-1.01879 1.01621-1.01879.45048 0 .75656.22069.96595.6993l2.16882 4.05042 2.17166-4.05524c.2069-.47379.513-.69448.9634-.69448.5603 0 1.0166.45711 1.0166 1.01879 0 .17579-.0465.35029-.1348.50523l-3.1697 5.8725c-.00503.0096-.01006.0184-.01509.0272-.00201.0036-.00402.0071-.00604.0106z"
                />
              </g>
            </svg>`}

        <span
          class="status-blip"
          style="background: linear-gradient(to right, ${this.getStatusColor(this.frontendStatus)} 50%, ${this.getStatusColor(this.javaStatus)} 50%)"
        ></span>
        ${this.splashMessage?_`<span class="status-description">${this.splashMessage}</span></div>`:T}
      </div>`}updated(t){var e;super.updated(t);const o=this.renderRoot.querySelector("#tabContainer"),i=[];if(this.tabs.forEach(s=>{s.element||(typeof s.render=="function"?s.element=document.createElement("div"):(s.element=document.createElement(s.render),s.element._devTools=this),i.push(s.element))}),(o==null?void 0:o.childElementCount)!==this.tabs.length){for(let s=0;s<this.tabs.length;s++){const r=this.tabs[s];o.childElementCount>s&&o.children[s]===r.element||o.insertBefore(r.element,o.children[s])}for(;(o==null?void 0:o.childElementCount)>this.tabs.length;)(e=o.lastElementChild)==null||e.remove()}for(const s of this.tabs){typeof s.render=="function"?we(s.render(),s.element):s.element.requestUpdate&&s.element.requestUpdate();const r=s.id===this.activeTab;s.element.hidden=!r}for(const s of i)for(var n=0;n<this.unhandledMessages.length;n++)this.tabHandleMessage(s,this.unhandledMessages[n])&&(this.unhandledMessages.splice(n,1),n--)}renderCode(){return _`<div class="info-tray">
      <div>
        <select id="locationType">
          <option value="create" selected>Create</option>
          <option value="attach">Attach</option>
        </select>
        <button
          class="button pick"
          @click=${async()=>{await f(()=>Promise.resolve().then(()=>pa),void 0),this.componentPicker.open({infoTemplate:_`
                <div>
                  <h3>Locate a component in source code</h3>
                  <p>Use the mouse cursor to highlight components in the UI.</p>
                  <p>Use arrow down/up to cycle through and highlight specific components under the cursor.</p>
                  <p>
                    Click the primary mouse button to open the corresponding source code line of the highlighted
                    component in your IDE.
                  </p>
                </div>
              `,pickCallback:t=>{const e={nodeId:t.nodeId,uiId:t.uiId};this.renderRoot.querySelector("#locationType").value==="create"?this.frontendConnection.sendShowComponentCreateLocation(e):this.frontendConnection.sendShowComponentAttachLocation(e)}})}}
        >
          Find component in code
        </button>
      </div>
      </div>
    </div>`}renderFeatures(){return _`<div class="features-tray">
      ${this.features.map(t=>_`<div class="feature">
          <label class="switch">
            <input
              class="feature-toggle"
              id="feature-toggle-${t.id}"
              type="checkbox"
              ?checked=${t.enabled}
              @change=${e=>this.toggleFeatureFlag(e,t)}
            />
            <span class="slider"></span>
            ${t.title}
          </label>
          <a class="ahreflike" href="${t.moreInfoLink}" target="_blank">Learn more</a>
        </div>`)}
    </div>`}disableJavaLiveReload(){var t;(t=this.javaConnection)==null||t.setActive(!1)}enableJavaLiveReload(){var t;(t=this.javaConnection)==null||t.setActive(!0)}renderThemeEditor(){return _` <vaadin-dev-tools-theme-editor
      .expanded=${this.expanded}
      .themeEditorState=${this.themeEditorState}
      .pickerProvider=${()=>this.componentPicker}
      .connection=${this.frontendConnection}
      @before-open=${this.disableJavaLiveReload}
      @after-close=${this.enableJavaLiveReload}
    ></vaadin-dev-tools-theme-editor>`}toggleFeatureFlag(t,e){const o=t.target.checked;this.frontendConnection?(this.frontendConnection.setFeature(e.id,o),this.showNotification(j.INFORMATION,`“${e.title}” ${o?"enabled":"disabled"}`,e.requiresServerRestart?"This feature requires a server restart":void 0,void 0,`feature${e.id}${o?"Enabled":"Disabled"}`)):this.log(j.ERROR,`Unable to toggle feature ${e.title}: No server connection available`)}};S.MAX_LOG_ROWS=1e3;S.DISMISSED_NOTIFICATIONS_IN_LOCAL_STORAGE="vaadin.live-reload.dismissedNotifications";S.ACTIVE_KEY_IN_SESSION_STORAGE="vaadin.live-reload.active";S.TRIGGERED_KEY_IN_SESSION_STORAGE="vaadin.live-reload.triggered";S.TRIGGERED_COUNT_KEY_IN_SESSION_STORAGE="vaadin.live-reload.triggeredCount";S.AUTO_DEMOTE_NOTIFICATION_DELAY=5e3;S.HOTSWAP_AGENT="HOTSWAP_AGENT";S.JREBEL="JREBEL";S.SPRING_BOOT_DEVTOOLS="SPRING_BOOT_DEVTOOLS";S.BACKEND_DISPLAY_NAME={HOTSWAP_AGENT:"HotswapAgent",JREBEL:"JRebel",SPRING_BOOT_DEVTOOLS:"Spring Boot Devtools"};m([w({type:String})],S.prototype,"url",void 0);m([w({type:Boolean,attribute:!0})],S.prototype,"liveReloadDisabled",void 0);m([w({type:String})],S.prototype,"backend",void 0);m([w({type:Number})],S.prototype,"springBootLiveReloadPort",void 0);m([w({type:Boolean,attribute:!1})],S.prototype,"expanded",void 0);m([w({type:Array,attribute:!1})],S.prototype,"messages",void 0);m([w({type:String,attribute:!1})],S.prototype,"splashMessage",void 0);m([w({type:Array,attribute:!1})],S.prototype,"notifications",void 0);m([w({type:String,attribute:!1})],S.prototype,"frontendStatus",void 0);m([w({type:String,attribute:!1})],S.prototype,"javaStatus",void 0);m([I()],S.prototype,"tabs",void 0);m([I()],S.prototype,"activeTab",void 0);m([I()],S.prototype,"features",void 0);m([I()],S.prototype,"unreadErrors",void 0);m([it(".window")],S.prototype,"root",void 0);m([it("vaadin-dev-tools-component-picker")],S.prototype,"componentPicker",void 0);m([I()],S.prototype,"componentPickActive",void 0);m([I()],S.prototype,"themeEditorState",void 0);S=k=m([V("vaadin-dev-tools")],S);export{nn as C,Js as D,ga as E,X as I,A as L,Z as N,ae as O,Ks as P,Ta as X,$a as Z,g as _,V as a,T as b,$ as c,Ys as d,Co as e,N as f,va as g,_ as h,mr as i,ur as j,xe as n,we as r,Ie as s,vs as u,vr as w,fa as x,pr as y};
